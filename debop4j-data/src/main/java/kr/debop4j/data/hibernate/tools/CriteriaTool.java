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

package kr.debop4j.data.hibernate.tools;

import kr.debop4j.core.tools.ArrayTool;
import kr.debop4j.core.tools.StringTool;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Criteria;
import org.hibernate.criterion.*;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import static kr.debop4j.core.Guard.shouldNotBeEmpty;
import static kr.debop4j.core.Guard.shouldNotBeNull;
import static org.hibernate.criterion.Restrictions.*;

/**
 * Criteria 관련 Utility Class 입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 9. 21
 */
@Slf4j
public final class CriteriaTool {

    private CriteriaTool() { }


    // region << Criterion >>


    /**
     * 특정 속성 값이 lowerValue 와 upperValue 구간에 속하는지 구합니다.
     *
     * @param propertyName the property name
     * @param lo           the lo
     * @param hi           the hi
     * @return the is between criterion
     */
    public static Criterion getIsBetweenCriterion(String propertyName, Object lo, Object hi) {
        return getIsBetweenCriterion(propertyName, lo, hi, true, true);
    }

    /**
     * Gets Between criterion.
     *
     * @param propertyName the property name
     * @param lo           the lo
     * @param hi           the hi
     * @param includeLo    the include lo
     * @param includeHi    the include hi
     * @return the is between criterion
     */
    public static Criterion getIsBetweenCriterion(String propertyName, Object lo, Object hi,
                                                  boolean includeLo, boolean includeHi) {
        shouldNotBeEmpty(propertyName, "propertyName");

        if (lo == null && hi == null)
            throw new IllegalArgumentException("상하한 값 모두 null 이면 안됩니다.");

        if (lo != null && hi != null && includeLo && includeHi)
            return between(propertyName, lo, hi);

        // lo, hi 값 중 하나가 없다면
        Conjunction result = conjunction();

        if (lo != null)
            result.add((includeLo) ? ge(propertyName, lo)
                               : gt(propertyName, lo));

        if (hi != null)
            result.add((includeHi) ? le(propertyName, hi)
                               : lt(propertyName, hi));

        return result;
    }

    /**
     * 지정한 값이 두 속성 값 사이에 존재하는지 여부
     *
     * @param loPropertyName the lo property name
     * @param hiPropertyName the hi property name
     * @param value          the value
     * @return the is in range criterion
     */
    public static Criterion getIsInRangeCriterion(final String loPropertyName,
                                                  final String hiPropertyName,
                                                  Object value) {
        return getIsInRangeCriterion(loPropertyName,
                                     hiPropertyName,
                                     value,
                                     true,
                                     true);
    }

    /**
     * 지정한 값이 두 속성 값 사이에 존재하는지 여부
     *
     * @param loPropertyName the lo property name
     * @param hiPropertyName the hi property name
     * @param value          the value
     * @param includeLo      the include lo
     * @param includeHi      the include hi
     * @return the is in range criterion
     */
    public static Criterion getIsInRangeCriterion(final String loPropertyName,
                                                  final String hiPropertyName,
                                                  Object value,
                                                  boolean includeLo,
                                                  boolean includeHi) {
        shouldNotBeNull(value, "value");

        Criterion loCriteria = (includeLo) ? le(loPropertyName, value)
                : lt(loPropertyName, value);
        Criterion hiCritiera = (includeHi) ? ge(hiPropertyName, value)
                : gt(hiPropertyName, value);

        return conjunction()
                .add(disjunction()
                             .add(isNull(loPropertyName))
                             .add(loCriteria))
                .add(disjunction()
                             .add(isNull(hiPropertyName))
                             .add(hiCritiera));
    }

    /**
     * 지정한 범위 값이 두 속성 값 구간과 겹치는지를 알아보기 위한 질의어
     *
     * @param loPropertyName the lo property name
     * @param hiPropertyName the hi property name
     * @param lo             the lo
     * @param hi             the hi
     * @return the is overlap criterion
     */
    public static Criterion getIsOverlapCriterion(final String loPropertyName,
                                                  final String hiPropertyName,
                                                  Object lo,
                                                  Object hi) {
        return getIsOverlapCriterion(loPropertyName, hiPropertyName, lo, hi, true, true);
    }

    /**
     * 지정한 범위 값이 두 속성 값 구간과 겹치는지를 알아보기 위한 질의어
     *
     * @param loPropertyName the lo property name
     * @param hiPropertyName the hi property name
     * @param lo             the lo
     * @param hi             the hi
     * @param includeLo      the include lo
     * @param includeHi      the include hi
     * @return the is overlap criterion
     */
    public static Criterion getIsOverlapCriterion(String loPropertyName,
                                                  String hiPropertyName,
                                                  Object lo,
                                                  Object hi,
                                                  boolean includeLo,
                                                  boolean includeHi) {
        if (lo == null && hi == null)
            throw new IllegalArgumentException("lo, hi 모두 null 값이면 질의어를 만들 수 없습니다.");

        if (log.isDebugEnabled())
            log.debug("build getIsOverlapCriterion... " +
                              "loPropertyName=[{}], hiPropertyName=[{}}] lo=[{}}], hi=[{}], includeLo=[{}}], includeHi=[{}]",
                      loPropertyName, hiPropertyName, lo, hi, includeLo, includeHi);

        if (lo != null && hi != null) {
            return Restrictions
                    .disjunction()
                    .add(getIsInRangeCriterion(loPropertyName, hiPropertyName, lo, includeLo, includeHi))
                    .add(getIsInRangeCriterion(loPropertyName, hiPropertyName, hi, includeLo, includeHi))
                    .add(getIsBetweenCriterion(loPropertyName, lo, hi, includeLo, includeHi))
                    .add(getIsBetweenCriterion(hiPropertyName, lo, hi, includeLo, includeHi));
        }

        if (lo != null) {
            return Restrictions
                    .disjunction()
                    .add(getIsInRangeCriterion(loPropertyName, hiPropertyName, lo, includeLo, includeHi))
                    .add((includeLo) ? ge(loPropertyName, lo)
                                 : gt(loPropertyName, lo))
                    .add((includeLo) ? ge(hiPropertyName, lo)
                                 : gt(hiPropertyName, lo));
        } else {
            return Restrictions
                    .disjunction()
                    .add(getIsInRangeCriterion(loPropertyName, hiPropertyName, hi, includeLo, includeHi))
                    .add((includeLo) ? le(loPropertyName, hi)
                                 : lt(loPropertyName, hi))
                    .add((includeLo) ? le(hiPropertyName, hi)
                                 : lt(hiPropertyName, hi));
        }
    }

    /**
     * value가 null 이 아니면, 속성값과 eq 이거나 null 인 경우 모두 구한다. value가 null 인 경우는 isNull 로 만든다.
     *
     * @param propertyName the property name
     * @param value        the value
     * @return the eq include null
     */
    public static Criterion getEqIncludeNull(final String propertyName, Object value) {
        if (value != null)
            return disjunction()
                    .add(eq(propertyName, value))
                    .add(isNull(propertyName));

        return isNull(propertyName);
    }

    /**
     * value 가 null 이면 isnull 을 null 이 아니면 eq 질의를 수행합니다.
     *
     * @param propertyName the property name
     * @param value        the value
     * @return the eq or null
     */
    public static Criterion getEqOrNull(final String propertyName, Object value) {
        return (value == null)
                ? isNull(propertyName)
                : eq(propertyName, value);
    }

    /**
     * Gets insensitive like include null.
     *
     * @param propertyName the property name
     * @param value        the value
     * @return the insensitive like include null
     */
    public static Criterion getInsensitiveLikeIncludeNull(final String propertyName, String value) {
        return getInsensitiveLikeIncludeNull(propertyName, value, MatchMode.START);
    }

    /**
     * Gets insensitive like include null.
     *
     * @param propertyName the property name
     * @param value        the value
     * @param matchMode    the match mode
     * @return the insensitive like include null
     */
    public static Criterion getInsensitiveLikeIncludeNull(String propertyName, String value, MatchMode matchMode) {
        if (StringTool.isWhiteSpace(value))
            return isEmpty(propertyName);

        return disjunction()
                .add(ilike(propertyName, value, matchMode))
                .add(isEmpty(propertyName));
    }


    // endregion

    // region << Criteria >>

    /**
     * Add eq.
     *
     * @param criteria     the criteria
     * @param propertyName the property name
     * @param value        the value
     * @return the criteria
     */
    public static Criteria addEq(Criteria criteria, String propertyName, Object value) {
        return criteria.add(getEqOrNull(propertyName, value));
    }

    /**
     * Add not eq.
     *
     * @param criteria     the criteria
     * @param propertyName the property name
     * @param value        the value
     * @return the criteria
     */
    public static Criteria addNotEq(Criteria criteria, String propertyName, Object value) {
        return criteria.add(not(getEqOrNull(propertyName, value)));
    }

    /**
     * Add le.
     *
     * @param criteria     the criteria
     * @param propertyName the property name
     * @param value        the value
     * @return the criteria
     */
    public static Criteria addLe(Criteria criteria, String propertyName, Object value) {
        shouldNotBeNull(value, "value");
        return criteria.add(le(propertyName, value));
    }

    /**
     * Add le property.
     *
     * @param criteria          the criteria
     * @param propertyName      the property name
     * @param otherPropertyName the other property name
     * @return the criteria
     */
    public static Criteria addLeProperty(Criteria criteria, String propertyName, String otherPropertyName) {
        return criteria.add(leProperty(propertyName, otherPropertyName));
    }

    /**
     * Add lt.
     *
     * @param criteria     the criteria
     * @param propertyName the property name
     * @param value        the value
     * @return the criteria
     */
    public static Criteria addLt(Criteria criteria, String propertyName, Object value) {
        shouldNotBeNull(value, "value");
        return criteria.add(lt(propertyName, value));
    }

    /**
     * Add lt property.
     *
     * @param criteria          the criteria
     * @param propertyName      the property name
     * @param otherPropertyName the other property name
     * @return the criteria
     */
    public static Criteria addLtProperty(Criteria criteria, String propertyName, String otherPropertyName) {
        return criteria.add(ltProperty(propertyName, otherPropertyName));
    }

    /**
     * Add ge.
     *
     * @param criteria     the criteria
     * @param propertyName the property name
     * @param value        the value
     * @return the criteria
     */
    public static Criteria addGe(Criteria criteria, String propertyName, Object value) {
        shouldNotBeNull(value, "value");
        return criteria.add(ge(propertyName, value));
    }

    /**
     * Add ge property.
     *
     * @param criteria          the criteria
     * @param propertyName      the property name
     * @param otherPropertyName the other property name
     * @return the criteria
     */
    public static Criteria addGeProperty(Criteria criteria, String propertyName, String otherPropertyName) {
        return criteria.add(geProperty(propertyName, otherPropertyName));
    }

    /**
     * Add gt.
     *
     * @param criteria     the criteria
     * @param propertyName the property name
     * @param value        the value
     * @return the criteria
     */
    public static Criteria addGt(Criteria criteria, String propertyName, Object value) {
        shouldNotBeNull(value, "value");
        return criteria.add(gt(propertyName, value));
    }

    /**
     * Add gt property.
     *
     * @param criteria          the criteria
     * @param propertyName      the property name
     * @param otherPropertyName the other property name
     * @return the criteria
     */
    public static Criteria addGtProperty(Criteria criteria, String propertyName, String otherPropertyName) {
        return criteria.add(gtProperty(propertyName, otherPropertyName));
    }

    /**
     * Add all eq.
     *
     * @param criteria           the criteria
     * @param propertyNameValues the property name values
     * @return the criteria
     */
    public static Criteria addAllEq(Criteria criteria,
                                    Map<String, Object> propertyNameValues) {
        shouldNotBeNull(propertyNameValues, "propertyNameValues");
        return criteria.add(allEq(propertyNameValues));
    }

    /**
     * Add is empty.
     *
     * @param criteria     the criteria
     * @param propertyName the property name
     * @return the criteria
     */
    public static Criteria addIsEmpty(Criteria criteria, String propertyName) {
        return criteria.add(isEmpty(propertyName));
    }

    /**
     * Add is not empty.
     *
     * @param criteria     the criteria
     * @param propertyName the property name
     * @return the criteria
     */
    public static Criteria addIsNotEmpty(Criteria criteria, String propertyName) {
        return criteria.add(isNotEmpty(propertyName));
    }

    /**
     * Add is null.
     *
     * @param criteria     the criteria
     * @param propertyName the property name
     * @return the criteria
     */
    public static Criteria addIsNull(Criteria criteria, String propertyName) {
        return criteria.add(isEmpty(propertyName));
    }

    /**
     * Add is not null.
     *
     * @param criteria     the criteria
     * @param propertyName the property name
     * @return the criteria
     */
    public static Criteria addIsNotNull(Criteria criteria, String propertyName) {
        return criteria.add(isNotEmpty(propertyName));
    }

    /**
     * Add like.
     *
     * @param criteria     the criteria
     * @param propertyName the property name
     * @param value        the value
     * @return the criteria
     */
    public static Criteria addLike(Criteria criteria, String propertyName, String value) {
        return addLike(criteria, propertyName, value, MatchMode.START);
    }

    /**
     * Add like.
     *
     * @param criteria     the criteria
     * @param propertyName the property name
     * @param value        the value
     * @param matchMode    the match mode
     * @return the criteria
     */
    public static Criteria addLike(Criteria criteria, String propertyName, String value, MatchMode matchMode) {
        return criteria.add(like(propertyName, value, matchMode));
    }

    /**
     * Insensitive Like search
     *
     * @param criteria     the criteria
     * @param propertyName the property name
     * @param value        the value
     * @return the criteria
     */
    public static Criteria addILike(Criteria criteria, String propertyName, String value) {
        return addILike(criteria, propertyName, value, MatchMode.START);
    }

    /**
     * Insensitive Like search
     *
     * @param criteria     the criteria
     * @param propertyName the property name
     * @param value        the value
     * @param matchMode    the match mode
     * @return the criteria
     */
    public static Criteria addILike(Criteria criteria, String propertyName, String value, MatchMode matchMode) {
        return criteria.add(ilike(propertyName, value, matchMode));
    }

    /**
     * Add id eq.
     *
     * @param criteria the criteria
     * @param idValue  the id value
     * @return the criteria
     */
    public static Criteria addIdEq(Criteria criteria, Serializable idValue) {
        shouldNotBeNull(idValue, "idValue");
        return criteria.add(idEq(idValue));
    }

    /**
     * Add in.
     *
     * @param criteria     the criteria
     * @param propertyName the property name
     * @param values       the values
     * @return the criteria
     */
    public static Criteria addIn(Criteria criteria, String propertyName, Collection values) {
        shouldNotBeNull(values, "values");
        return criteria.add(in(propertyName, values));
    }

    /**
     * Add in.
     *
     * @param criteria     the criteria
     * @param propertyName the property name
     * @param values       the values
     * @return the criteria
     */
    public static <T> Criteria addIn(Criteria criteria, String propertyName, T[] values) {
        assert !ArrayTool.isEmpty(values);
        return criteria.add(in(propertyName, values));
    }

    /**
     * Add between.
     *
     * @param criteria     the criteria
     * @param propertyName the property name
     * @param lo           the lo
     * @param hi           the hi
     * @return the criteria
     */
    public static Criteria addBetween(Criteria criteria, String propertyName, Object lo, Object hi) {
        return addBetween(criteria, propertyName, lo, hi, true, true);
    }

    /**
     * Add between.
     *
     * @param criteria     the criteria
     * @param propertyName the property name
     * @param lo           the lo
     * @param hi           the hi
     * @param includeLo    the include lo
     * @param includeHi    the include hi
     * @return the criteria
     */
    public static Criteria addBetween(Criteria criteria,
                                      final String propertyName,
                                      final Object lo,
                                      final Object hi,
                                      final boolean includeLo,
                                      final boolean includeHi) {

        return criteria.add(getIsBetweenCriterion(propertyName, lo, hi, includeLo, includeHi));
    }

    /**
     * Add in range.
     *
     * @param criteria       the criteria
     * @param loPropertyName the lo property name
     * @param hiPropertyName the hi property name
     * @param value          the value
     * @return the criteria
     */
    public static Criteria addInRange(Criteria criteria,
                                      final String loPropertyName,
                                      final String hiPropertyName,
                                      final Object value) {
        return criteria.add(getIsInRangeCriterion(loPropertyName, hiPropertyName, value));
    }

    /**
     * Add in range.
     *
     * @param criteria       the criteria
     * @param loPropertyName the lo property name
     * @param hiPropertyName the hi property name
     * @param value          the value
     * @param includeLo      the include lo
     * @param includeHi      the include hi
     * @return the criteria
     */
    public static Criteria addInRange(Criteria criteria,
                                      final String loPropertyName,
                                      final String hiPropertyName,
                                      final Object value,
                                      final boolean includeLo,
                                      final boolean includeHi) {
        return criteria.add(getIsInRangeCriterion(loPropertyName,
                                                  hiPropertyName,
                                                  value,
                                                  includeLo,
                                                  includeHi));
    }

    /**
     * Add is overlap.
     *
     * @param criteria       the criteria
     * @param loPropertyName the lo property name
     * @param hiPropertyName the hi property name
     * @param lo             the lo
     * @param hi             the hi
     * @return the criteria
     */
    public static Criteria addIsOverlap(Criteria criteria,
                                        final String loPropertyName,
                                        final String hiPropertyName,
                                        final Object lo,
                                        final Object hi) {
        return criteria.add(getIsOverlapCriterion(loPropertyName, hiPropertyName, lo, hi));
    }

    /**
     * Add is overlap.
     *
     * @param criteria       the criteria
     * @param loPropertyName the lo property name
     * @param hiPropertyName the hi property name
     * @param lo             the lo
     * @param hi             the hi
     * @param includeLo      the include lo
     * @param includeHi      the include hi
     * @return the criteria
     */
    public static Criteria addIsOverlap(Criteria criteria,
                                        final String loPropertyName,
                                        final String hiPropertyName,
                                        final Object lo,
                                        final Object hi,
                                        final boolean includeLo,
                                        final boolean includeHi) {
        return criteria.add(getIsOverlapCriterion(loPropertyName,
                                                  hiPropertyName,
                                                  lo,
                                                  hi,
                                                  includeLo,
                                                  includeHi));
    }

    /**
     * Add is elapsed.
     *
     * @param criteria     the criteria
     * @param propertyName the property name
     * @param date         the date
     * @return the criteria
     */
    public static Criteria addIsElapsed(Criteria criteria, String propertyName, Date date) {
        return criteria.add(lt(propertyName, date));
    }

    /**
     * Add null as false.
     *
     * @param criteria     the criteria
     * @param propertyName the property name
     * @param value        the value
     * @return the criteria
     */
    public static Criteria addNullAsFalse(Criteria criteria, String propertyName, Boolean value) {
        if (value == null || value)
            return addEq(criteria, propertyName, true);

        return criteria.add(getEqIncludeNull(propertyName, false));
    }

    /**
     * Add null as true.
     *
     * @param criteria     the criteria
     * @param propertyName the property name
     * @param value        the value
     * @return the criteria
     */
    public static Criteria addNullAsTrue(Criteria criteria, String propertyName, Boolean value) {
        if (value == null || !value)
            return addEq(criteria, propertyName, false);

        return criteria.add(getEqIncludeNull(propertyName, true));
    }

    /**
     * Add not.
     *
     * @param criteria   the criteria
     * @param expression the expression
     * @return the criteria
     */
    public static Criteria addNot(Criteria criteria, Criterion expression) {
        return criteria.add(Restrictions.not(expression));
    }

    // endregion

    // region << DetachedCriteria >>

    /**
     * Add eq.
     *
     * @param dc           the dc
     * @param propertyName the property name
     * @param value        the value
     * @return the detached criteria
     */
    public static DetachedCriteria addEq(DetachedCriteria dc, String propertyName, Object value) {
        return dc.add(getEqOrNull(propertyName, value));
    }

    /**
     * Add not eq.
     *
     * @param dc           the dc
     * @param propertyName the property name
     * @param value        the value
     * @return the detached criteria
     */
    public static DetachedCriteria addNotEq(DetachedCriteria dc, String propertyName, Object value) {
        return dc.add(not(getEqOrNull(propertyName, value)));
    }

    /**
     * Add le.
     *
     * @param dc           the dc
     * @param propertyName the property name
     * @param value        the value
     * @return the detached criteria
     */
    public static DetachedCriteria addLe(DetachedCriteria dc, String propertyName, Object value) {
        shouldNotBeNull(value, "value");
        return dc.add(le(propertyName, value));
    }

    /**
     * Add le property.
     *
     * @param dc                the dc
     * @param propertyName      the property name
     * @param otherPropertyName the other property name
     * @return the detached criteria
     */
    public static DetachedCriteria addLeProperty(DetachedCriteria dc, String propertyName, String otherPropertyName) {
        return dc.add(leProperty(propertyName, otherPropertyName));
    }

    /**
     * Add lt.
     *
     * @param dc           the dc
     * @param propertyName the property name
     * @param value        the value
     * @return the detached criteria
     */
    public static DetachedCriteria addLt(DetachedCriteria dc, String propertyName, Object value) {
        shouldNotBeNull(value, "value");
        return dc.add(lt(propertyName, value));
    }

    /**
     * Add lt property.
     *
     * @param dc                the dc
     * @param propertyName      the property name
     * @param otherPropertyName the other property name
     * @return the detached criteria
     */
    public static DetachedCriteria addLtProperty(DetachedCriteria dc, String propertyName, String otherPropertyName) {
        return dc.add(ltProperty(propertyName, otherPropertyName));
    }

    /**
     * Add ge.
     *
     * @param dc          the dc
     * @param proprtyName the proprty name
     * @param value       the value
     * @return the detached criteria
     */
    public static DetachedCriteria addGe(DetachedCriteria dc, String proprtyName, Object value) {
        shouldNotBeNull(value, "value");
        return dc.add(ge(proprtyName, value));
    }

    /**
     * Add ge property.
     *
     * @param dc                the dc
     * @param proprtyName       the proprty name
     * @param otherPropertyName the other property name
     * @return the detached criteria
     */
    public static DetachedCriteria addGeProperty(DetachedCriteria dc, String proprtyName, String otherPropertyName) {
        return dc.add(geProperty(proprtyName, otherPropertyName));
    }

    /**
     * Add gt.
     *
     * @param dc           the dc
     * @param propertyName the property name
     * @param value        the value
     * @return the detached criteria
     */
    public static DetachedCriteria addGt(DetachedCriteria dc, String propertyName, Object value) {
        shouldNotBeNull(value, "value");
        return dc.add(gt(propertyName, value));
    }

    /**
     * Add gt property.
     *
     * @param dc                the dc
     * @param propertyName      the property name
     * @param otherPropertyName the other property name
     * @return the detached criteria
     */
    public static DetachedCriteria addGtProperty(DetachedCriteria dc,
                                                 String propertyName,
                                                 String otherPropertyName) {
        return dc.add(gtProperty(propertyName, otherPropertyName));
    }

    /**
     * Add all eq.
     *
     * @param dc                 the dc
     * @param propertyNameValues the property name values
     * @return the detached criteria
     */
    public static DetachedCriteria addAllEq(DetachedCriteria dc,
                                            Map<String, Object> propertyNameValues) {
        shouldNotBeNull(propertyNameValues, "propertyNameValues");
        return dc.add(allEq(propertyNameValues));
    }

    /**
     * Add is empty.
     *
     * @param dc           the dc
     * @param propertyName the property name
     * @return the detached criteria
     */
    public static DetachedCriteria addIsEmpty(DetachedCriteria dc, String propertyName) {
        return dc.add(isEmpty(propertyName));
    }

    /**
     * Add is not empty.
     *
     * @param dc           the dc
     * @param propertyName the property name
     * @return the detached criteria
     */
    public static DetachedCriteria addIsNotEmpty(DetachedCriteria dc, String propertyName) {
        return dc.add(Restrictions.isNotEmpty(propertyName));
    }

    /**
     * Add is null.
     *
     * @param dc           the dc
     * @param propertyName the property name
     * @return the detached criteria
     */
    public static DetachedCriteria addIsNull(DetachedCriteria dc, String propertyName) {
        return dc.add(isEmpty(propertyName));
    }

    /**
     * Add is not null.
     *
     * @param dc           the dc
     * @param propertyName the property name
     * @return the detached criteria
     */
    public static DetachedCriteria addIsNotNull(DetachedCriteria dc, String propertyName) {
        return dc.add(Restrictions.isNotEmpty(propertyName));
    }

    /**
     * Add like.
     *
     * @param dc           the dc
     * @param propertyName the property name
     * @param value        the value
     * @return the detached criteria
     */
    public static DetachedCriteria addLike(DetachedCriteria dc, String propertyName, String value) {
        return addLike(dc, propertyName, value, MatchMode.START);
    }

    /**
     * Add like.
     *
     * @param dc           the dc
     * @param propertyName the property name
     * @param value        the value
     * @param matchMode    the match mode
     * @return the detached criteria
     */
    public static DetachedCriteria addLike(DetachedCriteria dc, String propertyName, String value, MatchMode matchMode) {
        return dc.add(Restrictions.like(propertyName, value, matchMode));
    }

    /**
     * Insensitive Like search
     *
     * @param dc           the dc
     * @param propertyName the property name
     * @param value        the value
     * @return the detached criteria
     */
    public static DetachedCriteria addILike(DetachedCriteria dc, String propertyName, String value) {
        return addILike(dc, propertyName, value, MatchMode.START);
    }

    /**
     * Insensitive Like search
     *
     * @param dc           the dc
     * @param propertyName the property name
     * @param value        the value
     * @param matchMode    the match mode
     * @return the detached criteria
     */
    public static DetachedCriteria addILike(DetachedCriteria dc, String propertyName, String value, MatchMode matchMode) {
        return dc.add(Restrictions.ilike(propertyName, value, matchMode));
    }

    /**
     * Add id eq.
     *
     * @param dc      the dc
     * @param idValue the id value
     * @return the detached criteria
     */
    public static DetachedCriteria addIdEq(DetachedCriteria dc, Serializable idValue) {
        return dc.add(Restrictions.idEq(idValue));
    }

    /**
     * Add in.
     *
     * @param dc           the dc
     * @param propertyName the property name
     * @param values       the values
     * @return the detached criteria
     */
    public static DetachedCriteria addIn(DetachedCriteria dc, String propertyName, Collection values) {
        return dc.add(Restrictions.in(propertyName, values));
    }

    /**
     * Add in.
     *
     * @param dc           the dc
     * @param propertyName the property name
     * @param values       the values
     * @return the detached criteria
     */
    public static <T> DetachedCriteria addIn(DetachedCriteria dc, String propertyName, T[] values) {
        return dc.add(Restrictions.in(propertyName, values));
    }

    /**
     * Add between.
     *
     * @param dc           the dc
     * @param propertyName the property name
     * @param lo           the lo
     * @param hi           the hi
     * @return the detached criteria
     */
    public static DetachedCriteria addBetween(DetachedCriteria dc, String propertyName, Object lo, Object hi) {
        return addBetween(dc, propertyName, lo, hi, true, true);
    }

    /**
     * Add between.
     *
     * @param dc           the dc
     * @param propertyName the property name
     * @param lo           the lo
     * @param hi           the hi
     * @param includeLo    the include lo
     * @param includeHi    the include hi
     * @return the detached criteria
     */
    public static DetachedCriteria addBetween(DetachedCriteria dc,
                                              final String propertyName,
                                              final Object lo,
                                              final Object hi,
                                              final boolean includeLo,
                                              final boolean includeHi) {

        return dc.add(getIsBetweenCriterion(propertyName, lo, hi, includeLo, includeHi));
    }

    /**
     * Add in range.
     *
     * @param dc             the dc
     * @param loPropertyName the lo property name
     * @param hiPropertyName the hi property name
     * @param value          the value
     * @return the detached criteria
     */
    public static DetachedCriteria addInRange(DetachedCriteria dc,
                                              final String loPropertyName,
                                              final String hiPropertyName,
                                              final Object value) {
        return dc.add(getIsInRangeCriterion(loPropertyName,
                                            hiPropertyName,
                                            value));
    }

    /**
     * Add in range.
     *
     * @param dc             the dc
     * @param loPropertyName the lo property name
     * @param hiPropertyName the hi property name
     * @param value          the value
     * @param includeLo      the include lo
     * @param includeHi      the include hi
     * @return the detached criteria
     */
    public static DetachedCriteria addInRange(DetachedCriteria dc,
                                              final String loPropertyName,
                                              final String hiPropertyName,
                                              final Object value,
                                              final boolean includeLo,
                                              final boolean includeHi) {
        return dc.add(getIsInRangeCriterion(loPropertyName,
                                            hiPropertyName,
                                            value,
                                            includeLo,
                                            includeHi));
    }

    /**
     * Add is overlap.
     *
     * @param dc             the dc
     * @param loPropertyName the lo property name
     * @param hiPropertyName the hi property name
     * @param lo             the lo
     * @param hi             the hi
     * @return the detached criteria
     */
    public static DetachedCriteria addIsOverlap(DetachedCriteria dc,
                                                final String loPropertyName,
                                                final String hiPropertyName,
                                                final Object lo,
                                                final Object hi) {
        return dc.add(getIsOverlapCriterion(loPropertyName, hiPropertyName, lo, hi));
    }

    /**
     * Add is overlap.
     *
     * @param dc             the dc
     * @param loPropertyName the lo property name
     * @param hiPropertyName the hi property name
     * @param lo             the lo
     * @param hi             the hi
     * @param includeLo      the include lo
     * @param includeHi      the include hi
     * @return the detached criteria
     */
    public static DetachedCriteria addIsOverlap(DetachedCriteria dc,
                                                final String loPropertyName,
                                                final String hiPropertyName,
                                                final Object lo,
                                                final Object hi,
                                                final boolean includeLo,
                                                final boolean includeHi) {
        return dc.add(getIsOverlapCriterion(loPropertyName,
                                            hiPropertyName,
                                            lo,
                                            hi,
                                            includeLo,
                                            includeHi));
    }

    /**
     * Add is elapsed.
     *
     * @param dc           the dc
     * @param propertyName the property name
     * @param date         the date
     * @return the detached criteria
     */
    public static DetachedCriteria addIsElapsed(DetachedCriteria dc, String propertyName, Date date) {
        return dc.add(lt(propertyName, date));
    }

    /**
     * Add null as false.
     *
     * @param dc           the dc
     * @param propertyName the property name
     * @param value        the value
     * @return the detached criteria
     */
    public static DetachedCriteria addNullAsFalse(DetachedCriteria dc, String propertyName, Boolean value) {
        if (value == null || value)
            return addEq(dc, propertyName, true);

        return dc.add(getEqIncludeNull(propertyName, false));
    }

    /**
     * Add null as true.
     *
     * @param dc           the dc
     * @param propertyName the property name
     * @param value        the value
     * @return the detached criteria
     */
    public static DetachedCriteria addNullAsTrue(DetachedCriteria dc, String propertyName, Boolean value) {
        if (value == null || !value)
            return addEq(dc, propertyName, false);

        return dc.add(getEqIncludeNull(propertyName, true));
    }

    /**
     * Add not.
     *
     * @param dc         the dc
     * @param expression the expression
     * @return the detached criteria
     */
    public static DetachedCriteria addNot(DetachedCriteria dc, Criterion expression) {
        return dc.add(Restrictions.not(expression));
    }

    // endregion


}
