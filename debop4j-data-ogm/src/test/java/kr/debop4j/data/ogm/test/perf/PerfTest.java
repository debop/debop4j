package kr.debop4j.data.ogm.test.perf;

import kr.debop4j.data.ogm.test.simpleentity.OgmTestBase;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.data.ogm.test.perf.PerfTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 1
 */
@Slf4j
public class PerfTest extends OgmTestBase {

    private static Random rand = new Random();

    private AtomicInteger authorId;
    private AtomicInteger blogId;
    private AtomicLong blogEntryId;

    @Override
    protected Class<?>[] getAnnotatedClasses() {
        return new Class<?>[] {
                Author.class,
                Blog.class,
                BlogEntry.class
        };
    }

    private int randId(int nbrOfAuthors) {
        return nbrOfAuthors > 1 ? rand.nextInt(nbrOfAuthors - 1) + 1 : 1;
    }

    @Override
    public void doBefore() throws Exception {
        super.doBefore();

        authorId = new AtomicInteger(0);
        blogId = new AtomicInteger(0);
        blogEntryId = new AtomicLong(0L);
    }

    @Test
    public void collectionAssociationPerf() throws Exception {
        log.debug("Warm up...");

        Session session = openSession();

        int nbrOfAuthors = 100;
        if (nbrOfAuthors >= 200) {
            for (int j = 0; j < nbrOfAuthors / 200; j++) {
                save200AuthorsAndCommit(session, 200);
                save200BlogsAndCommit(session, 200);
            }
        } else {
            save200AuthorsAndCommit(session, nbrOfAuthors);
            save200BlogsAndCommit(session, nbrOfAuthors);
        }

        int nbrOfBlogEntries = 35000; // 350000;
        System.out.printf("Warm up period done\nSaving %s Blog entries\n", nbrOfBlogEntries);
        long start = System.nanoTime();

        for (int j = 0; j < nbrOfBlogEntries / 200; j++) {
            save200BlogEntriesAndCommit(session, nbrOfAuthors, true);
        }

        System.out.printf("Writing %s took %sms ie %sns/entry\n", nbrOfBlogEntries, (System.nanoTime() - start) / 1000000, (System.nanoTime() - start) / (nbrOfBlogEntries));
        System.out.printf("Collection ratio %s entries per collection\n", nbrOfBlogEntries / nbrOfAuthors);

        session.close();

        session = openSession();

        int nbr_of_reads = nbrOfBlogEntries / 200;
        start = System.nanoTime();
        for (int i = 0; i < nbr_of_reads; i++) {
            int primaryKey = rand.nextInt(nbrOfBlogEntries - 1) + 1; //start from 1
            BlogEntry blogEntry = (BlogEntry) session.get(BlogEntry.class, Long.valueOf(primaryKey));
            assertThat(blogEntry.getContent()).isNotEmpty();
            assertThat(blogEntry.getId()).isEqualTo(primaryKey);
            assertThat(blogEntry.getAuthor()).isNotNull();
            assertThat(blogEntry.getAuthor().getFname()).isNotEmpty();
            assertThat(blogEntry.getBlog().getTitle()).isNotEmpty();
        }
        System.out.printf("Reading %s took %sms ie %sns/entry\n", nbr_of_reads, (System.nanoTime() - start) / 1000000, (System.nanoTime() - start) / (nbr_of_reads));

        session.clear();
        start = System.nanoTime();
        int blogReads = nbrOfAuthors * 10;
        if (blogReads > 10000) {
            blogReads = 10000;
        }
        for (int i = 0; i < blogReads; i++) {
            Transaction transaction = session.beginTransaction();
            int primaryKey = randId(nbrOfAuthors);
            Blog blog = (Blog) session.get(Blog.class, primaryKey);

            assertThat(blog.getTitle()).isNotEmpty();
            assertThat(blog.getId()).isEqualTo(primaryKey);
            assertThat(blog.getEntries()).isNotNull();
            if (blog.getEntries().size() < (nbrOfBlogEntries / nbrOfAuthors) / 10) {
                System.out.printf("Small number of entries in this collection %s\n", blog.getEntries().size());
            }
            transaction.commit();
            session.clear();
        }
        System.out.printf("Reading from blog %s times took %sms ie %sns/entry\n", blogReads, (System.nanoTime() - start) / 1000000, (System.nanoTime() - start) / blogReads);
        session.close();
    }

    @Test
    public void manyToOneAssociations() throws Exception {
        System.out.printf("Warming up\n");
        Session session = openSession();

        int nbrOfAuthors = 5000; // 50000;
        for (int j = 0; j < nbrOfAuthors / 200; j++) {
            save200AuthorsAndCommit(session, 200);
        }

        int nbrOfBlogEntries = 35000; //350000;
        System.out.printf("Warm up period done\nSaving %s Blog entries\n", nbrOfBlogEntries);
        long start = System.nanoTime();

        for (int j = 0; j < nbrOfBlogEntries / 200; j++) {
            save200BlogEntriesAndCommit(session, nbrOfAuthors, false);
        }

        System.out.printf("Writing %s took %sms ie %sns/entry\n", nbrOfBlogEntries, (System.nanoTime() - start) / 1000000, (System.nanoTime() - start) / (nbrOfBlogEntries));

        int nbr_of_reads = 10000; // 100000;
        start = System.nanoTime();
        for (int i = 0; i < nbr_of_reads; i++) {
            int primaryKey = rand.nextInt(nbrOfBlogEntries - 1) + 1; //start from 1
            BlogEntry blog = (BlogEntry) session.get(BlogEntry.class, Long.valueOf(primaryKey));
            assertThat(blog.getContent()).isNotEmpty();
            assertThat(blog.getId()).isEqualTo(primaryKey);
            assertThat(blog.getAuthor()).isNotNull();
            assertThat(blog.getAuthor().getFname()).isNotEmpty();
        }
        System.out.printf("Reading %s took %sms ie %sns/entry\n", nbr_of_reads, (System.nanoTime() - start) / 1000000, (System.nanoTime() - start) / (nbr_of_reads));


        session.close();
    }

    @Test
    public void simpleEntityInserts() throws Exception {
        Session session = openSession();

        int authors = 20000; //2000000;
        System.out.printf("Warming up\n");
        for (int j = 0; j < 200; j++) {
            save200AuthorsAndCommit(session, 200);
        }

        System.out.printf("Warm up period done\nSaving %s entities\n", authors);
        long start = System.nanoTime();

        for (int j = 0; j < authors / 200; j++) {
            save200AuthorsAndCommit(session, 200);
        }
        System.out.printf("Saving %s took %sms ie %sns/entry\n", authors, (System.nanoTime() - start) / 1000000, (System.nanoTime() - start) / (authors));
        session.close();

        session = openSession();
        int nbr_of_reads = 1000; //100000;
        start = System.nanoTime();
        for (int i = 0; i < nbr_of_reads; i++) {
            int primaryKey = rand.nextInt(authors - 1) + 1; //start from 1
            Author author = (Author) session.get(Author.class, primaryKey);
            if (author == null) {
                System.out.printf("Cannot find author %s, %sth loop\n", primaryKey, i);
            } else {
                assertThat(author.getBio()).isNotEmpty();
                assertThat(author.getId()).isEqualTo(primaryKey);
            }
        }
        System.out.printf("Reading %s took %sms ie %sns/entry\n", nbr_of_reads, (System.nanoTime() - start) / 1000000, (System.nanoTime() - start) / (nbr_of_reads));

        session.close();
    }

    private void save200AuthorsAndCommit(Session session, int nbrOfAuthors) throws Exception {

        Transaction transaction = session.beginTransaction();

        for (int i = 0; i < nbrOfAuthors; i++) {
            Author author = new Author();
            author.setId(authorId.incrementAndGet());
            author.setBio("This is a decent size bio made of " + rand.nextDouble() + " stuffs");
            author.setDob(new Date());
            author.setFname("Emmanuel " + rand.nextInt());
            author.setLname("Bernard " + rand.nextInt());
            author.setMname("" + rand.nextInt(26));

            session.persist(author);
        }

        transaction.commit();
        session.clear();
    }

    private void save200BlogsAndCommit(Session session, int nbrOfBlogs) throws Exception {
        Transaction transaction = session.beginTransaction();
        for (int i = 0; i < nbrOfBlogs; i++) {
            Blog blog = new Blog();
            blog.setId(blogId.incrementAndGet());
            blog.setTitle("My life in a blog" + rand.nextDouble());
            blog.setDescription("I will describe what's happening in my life and ho things are going " + rand.nextDouble());

            session.persist(blog);
        }
        transaction.commit();
        session.clear();
    }

    private void save200BlogEntriesAndCommit(Session session, int nbrOfAuthors, boolean alsoAddBlog) throws Exception {
        Transaction transaction = session.beginTransaction();
        for (int i = 0; i < 200; i++) {
            BlogEntry blogEntry = new BlogEntry();
            blogEntry.setId(blogEntryId.incrementAndGet());
            blogEntry.setTitle("This is a blog of " + rand.nextDouble());
            blogEntry.setContent("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas bibendum risus commodo purus pellentesque quis venenatis enim tincidunt. Maecenas at nisl in nunc eleifend rutrum eu sit amet urna. Nulla dui diam, mollis a facilisis nec, iaculis feugiat lectus. Donec egestas, dui id facilisis euismod, lorem dui ornare est, vel feugiat ipsum odio et augue. Phasellus laoreet quam et augue hendrerit interdum cursus urna sodales. Cras eleifend mollis pharetra. Donec lectus sapien, ultricies eu fermentum sed, tempor nec odio.\n" +
                                         "\n" +
                                         rand.nextDouble() +
                                         "Proin ullamcorper bibendum leo, ut luctus turpis sodales nec. Sed diam augue, malesuada quis dapibus eu, convallis et ligula. Duis eget vehicula quam. Quisque id mauris non nisl mattis tempus a non augue. In ut purus orci, vitae eleifend ipsum. Praesent convallis fringilla massa non tincidunt. Duis eget erat venenatis purus iaculis accumsan eu et mauris. Mauris id risus et erat consequat eleifend vitae in nisl. Integer consequat, velit vel dapibus posuere, nisl magna semper tellus, ut commodo felis purus at ipsum. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas.\n" +
                                         "\n" +
                                         "Ut sodales purus sit amet sapien semper sagittis. Duis aliquam tempus dictum. Cras suscipit ullamcorper cursus. Nam mollis lacinia aliquam. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Maecenas quis risus est, sit amet iaculis sapien. Ut nibh sapien, ornare ac mattis et, scelerisque eu leo. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas.\n" +
                                         "\n" +
                                         "Nulla in elit in felis viverra venenatis id a ligula. Nunc nec odio felis, vel ultricies metus. Morbi placerat porta elementum. Vestibulum a lacinia lectus. Nunc mauris nunc, luctus non mattis ac, venenatis vitae nulla. Sed risus est, imperdiet vitae molestie in, ullamcorper nec urna. Donec eu risus sem. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Nulla.");
            int randId = randId(nbrOfAuthors);
            blogEntry.setAuthor((Author) session.load(Author.class, randId));
            if (alsoAddBlog) {
                randId = randId(nbrOfAuthors);
                blogEntry.setBlog((Blog) session.load(Blog.class, randId));
            }
            session.persist(blogEntry);
        }
        transaction.commit();
        session.clear();
    }
}
