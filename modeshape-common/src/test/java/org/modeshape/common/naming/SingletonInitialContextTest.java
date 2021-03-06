/*
 * ModeShape (http://www.modeshape.org)
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.  Some portions may be licensed
 * to Red Hat, Inc. under one or more contributor license agreements.
 * See the AUTHORS.txt file in the distribution for a full listing of 
 * individual contributors. 
 *
 * ModeShape is free software. Unless otherwise indicated, all code in ModeShape
 * is licensed to you under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * ModeShape is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.modeshape.common.naming;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.junit.Assert.assertThat;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import junit.framework.AssertionFailedError;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Randall Hauch
 */
public class SingletonInitialContextTest {

    private String validName;
    private Object registeredObject;

    @Before
    public void beforeEach() {
        this.validName = "java:jboss/unit/test/name";
        this.registeredObject = "This is the registered object";
    }

    @After
    public void afterEach() {
        SingletonInitialContextFactory.tearDown();
    }

    @Test
    public void shouldCreateInitialContextAndRegisterAnObject() throws Exception {
        SingletonInitialContext.register(this.validName, this.registeredObject);
        for (int i = 0; i != 10; ++i) {
            assertThat(new InitialContext().lookup(this.validName), is(sameInstance(this.registeredObject)));
        }
    }

    @Test
    public void shouldTearDownMockInitialContextUponRequest() throws Exception {
        // Set it up ...
        // (Don't want to use 'expected', since the NamingException could be thrown here and we wouldn't know the difference)
        SingletonInitialContext.register(this.validName, this.registeredObject);
        for (int i = 0; i != 10; ++i) {
            assertThat(new InitialContext().lookup(this.validName), is(sameInstance(this.registeredObject)));
        }
        // Tear it down ...
        SingletonInitialContextFactory.tearDown();
        try {
            new InitialContext().lookup(this.validName);
            throw new AssertionFailedError("Failed to throw exception");
        } catch (NameNotFoundException e) {
            // expected
        }
    }

}
