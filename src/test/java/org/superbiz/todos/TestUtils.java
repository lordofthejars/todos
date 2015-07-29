/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the &quot;License&quot;);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.superbiz.todos;

import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

public class TestUtils {
    static WebArchive createWebArchive() {
        return createWebArchive("angular-test.war");
    }
    static WebArchive createWebArchive(String archiveName) {
        GenericArchive webAppDirectory = ShrinkWrap
                .create(GenericArchive.class).as(ExplodedImporter.class)
                .importDirectory("src/main/resources/app").as(GenericArchive.class);

        return ShrinkWrap.create(WebArchive.class, archiveName)
                 .addClasses(ApplicationConfig.class, UiApplication.class, Data.class, ListProducer.class)
                 .merge(webAppDirectory, "/", Filters.include(".*\\.(js|css|html|xml)$"))
                 .addAsLibraries(Maven.resolver().resolve("org.glassfish:javax.json:1.0.4")
                     .withTransitivity().as(JavaArchive.class))
                 .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }
}
