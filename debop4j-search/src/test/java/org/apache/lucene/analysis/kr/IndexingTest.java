/*
 * Copyright 2011-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.lucene.analysis.kr;

import junit.framework.TestCase;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;

public class IndexingTest extends TestCase {

    private Directory directory;

    protected void setUp() throws Exception {
        directory = FSDirectory.open(new File(".lucene/index"));
    }

    private IndexWriter getWriter() throws IOException {
        return new IndexWriter(directory, new KoreanAnalyzer(), IndexWriter.MaxFieldLength.UNLIMITED);
    }

    public void testIndexWriter() throws IOException {

        IndexWriter writer = getWriter();

        String description = "Approved for entry into archive by p pant (momo31@gmail.com) on 2011-11-18T05:08:46Z (GMT) No. of bitstreams: 0";
        String publisher = "漢陽大學校";
        String title = "硏究開發費 會計에 關한 硏究";


        Document doc = new Document();
        doc.add(new Field("description", description, Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field("publisher", publisher, Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field("title", title, Field.Store.YES, Field.Index.ANALYZED));

        writer.addDocument(doc);

        writer.close();

    }

}
