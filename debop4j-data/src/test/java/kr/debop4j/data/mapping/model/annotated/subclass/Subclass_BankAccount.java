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

package kr.debop4j.data.mapping.model.annotated.subclass;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * org.annotated.mapping.domain.model.subclass.Subclass_BankAccount
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 12. 4.
 */
@Entity
@DiscriminatorValue("BA")
@Getter
@Setter
public class Subclass_BankAccount extends Subclass_BillingDetails {

    private static final long serialVersionUID = 6159765179966313199L;

    @Column(name = "BANK_ACCOUNT")
    private String account;

    @Column(name = "BANK_NAME")
    private String bankname;

    @Column(name = "BANK_SWIFT")
    private String swift;
}
