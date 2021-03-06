/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Author: Ricard Gavalda, http://www.lsi.upc.edu/gavalda
 * Date: April 10th, 2012
 * Tested on 3.6.0. WILL NOT WORK with lucene 4.x and higher
 * Based on the org.lucene.demo.SearchFiles class
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

//import org.apache.lucene.analysis.Analyzer;
//import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.index.Term;
//import org.apache.lucene.queryParser.QueryParser;
//import org.apache.lucene.search.IndexSearcher;
//import org.apache.lucene.search.Query;
//import org.apache.lucene.search.ScoreDoc;
//import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
//import org.apache.lucene.util.Version;

/* Open an index given as argument, and print to standard output
 * all the terms contained in its "contents" field, together with their
 * total count (not document count, but total number of occurrences 
 * in the indexed document collection). Order as provided by the index */
public class CountWords3 {

  private CountWords3() {}

  public static void main(String[] args) throws Exception {

    if (args.length != 1) {
       String usage = "Usage:\tjava CountWords3 indexdir";
       System.out.println(usage);
       System.exit(0);
    }

    String index = args[0];
    String field = "contents";
    String queries = null;
        
    IndexReader reader = IndexReader.open(FSDirectory.open(new File(index)));
    TermEnum te = reader.terms();

    int totalOccs = 0;
    int i = 0;
    boolean notlastt = te.next();
    while (notlastt) {
        Term t = te.term();
        if (t.field() == field) {   // ignore if not desired field
           TermDocs td = reader.termDocs(t); 
           int n = 0;
           boolean notlastd = td.next();
           while (notlastd) {
               n += td.freq();
               notlastd = td.next();
           }
           System.out.println(t.text()+" "+n);
           totalOccs += n;
           ++i;
        }
        notlastt = te.next();
    }
    System.out.println("Distinct words: "+i+"; Word occurrences: "+totalOccs);
  }
}
