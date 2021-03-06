/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2016 by Pentaho : http://www.pentaho.com
 *
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/
package org.pentaho.di.trans.steps.fuzzymatch;

import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.*;

import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.trans.steps.loadsave.LoadSaveTester;
import org.pentaho.di.trans.steps.loadsave.validator.ArrayLoadSaveValidator;
import org.pentaho.di.trans.steps.loadsave.validator.FieldLoadSaveValidator;
import org.pentaho.di.trans.steps.loadsave.validator.StringLoadSaveValidator;

public class FuzzyMatchMetaTest {

  LoadSaveTester loadSaveTester;

  @Before
  public void setUp() throws Exception {
    List<String> attributes =
        Arrays.asList( "value", "valueName", "algorithm", "lookupfield", "mainstreamfield",
            "outputmatchfield", "outputvaluefield", "caseSensitive", "minimalValue",
            "maximalValue", "separator", "closervalue" );

    Map<String, String> getterMap = new HashMap<String, String>() {
      {
        put( "value", "getValue" );
        put( "valueName", "getValueName" );
        put( "algorithm", "getAlgorithmType" );
        put( "lookupfield", "getLookupField" );
        put( "mainstreamfield", "getMainStreamField" );
        put( "outputmatchfield", "getOutputMatchField" );
        put( "outputvaluefield", "getOutputValueField" );
        put( "caseSensitive", "isCaseSensitive" );
        put( "minimalValue", "getMinimalValue" );
        put( "maximalValue", "getMaximalValue" );
        put( "separator", "getSeparator" );
        put( "closervalue", "isGetCloserValue" );
      }
    };

    Map<String, String> setterMap = new HashMap<String, String>() {
      {
        put( "value", "setValue" );
        put( "valueName", "setValueName" );
        put( "algorithm", "setAlgorithmType" );
        put( "lookupfield", "setLookupField" );
        put( "mainstreamfield", "setMainStreamField" );
        put( "outputmatchfield", "setOutputMatchField" );
        put( "outputvaluefield", "setOutputValueField" );
        put( "caseSensitive", "setCaseSensitive" );
        put( "minimalValue", "setMinimalValue" );
        put( "maximalValue", "setMaximalValue" );
        put( "separator", "setSeparator" );
        put( "closervalue", "setGetCloserValue" );
      }
    };
    FieldLoadSaveValidator<String[]> stringArrayLoadSaveValidator =
        new ArrayLoadSaveValidator<String>( new StringLoadSaveValidator(), 3 );
    Map<String, FieldLoadSaveValidator<?>> attrValidatorMap = new HashMap<String, FieldLoadSaveValidator<?>>();
    attrValidatorMap.put( "value", stringArrayLoadSaveValidator );
    attrValidatorMap.put( "valueName", stringArrayLoadSaveValidator );
    attrValidatorMap.put( "algorithm", new AlgorithmLoadSaveValidator() );

    Map<String, FieldLoadSaveValidator<?>> typeValidatorMap = new HashMap<String, FieldLoadSaveValidator<?>>();
    // typeValidatorMap.put( int[].class.getCanonicalName(), new PrimitiveIntArrayLoadSaveValidator( new IntLoadSaveValidator(), 3 ) );

    loadSaveTester = new LoadSaveTester( FuzzyMatchMeta.class, attributes, getterMap, setterMap, attrValidatorMap, typeValidatorMap );
  }

  @Test
  public void testLoadSaveXML() throws KettleException {
    loadSaveTester.testXmlRoundTrip();
  }

  @Test
  public void testLoadSaveRepo() throws KettleException {
    loadSaveTester.testRepoRoundTrip();
  }

  @Test
  public void cloneTest() throws Exception {
    FuzzyMatchMeta meta = new FuzzyMatchMeta();
    meta.allocate( 2 );
    meta.setValue( new String[] { "aa", "bb" } );
    meta.setValueName( new String[] { "cc", "dd" } );
    meta.setAlgorithmType( 1 );
    meta.setCaseSensitive( true );
    meta.setLookupField( "lookupfield" );
    meta.setMainStreamField( "mainstreamfield" );
    meta.setMaximalValue( "maximalValue" );
    meta.setMinimalValue( "minimalValue" );
    meta.setOutputMatchField( "outputmatchfield" );
    meta.setOutputValueField( "outputvaluefield" );
    meta.setSeparator( ";" );
    FuzzyMatchMeta aClone = (FuzzyMatchMeta) meta.clone();
    assertFalse( aClone == meta );
    assertTrue( Arrays.equals( meta.getValue(), aClone.getValue() ) );
    assertTrue( Arrays.equals( meta.getValueName(), aClone.getValueName() ) );
    assertEquals( meta.getAlgorithmType(), aClone.getAlgorithmType() );
    assertEquals( meta.isCaseSensitive(), aClone.isCaseSensitive() );
    assertEquals( meta.getLookupField(), aClone.getLookupField() );
    assertEquals( meta.getMainStreamField(), aClone.getMainStreamField() );
    assertEquals( meta.getMaximalValue(), aClone.getMaximalValue() );
    assertEquals( meta.getMinimalValue(), aClone.getMinimalValue() );
    assertEquals( meta.getOutputMatchField(), aClone.getOutputMatchField() );
    assertEquals( meta.getOutputValueField(), aClone.getOutputValueField() );
    assertEquals( meta.getXML(), aClone.getXML() );
  }

  public class AlgorithmLoadSaveValidator implements FieldLoadSaveValidator<Integer> {
    final Random rand = new Random();
    @Override
    public Integer getTestObject() {
      return rand.nextInt( 10 );
    }
    @Override
    public boolean validateTestObject( Integer testObject, Object actual ) {
      if ( !( actual instanceof Integer ) ) {
        return false;
      }
      Integer actualInt = (Integer) actual;
      return actualInt.equals( testObject );
    }
  }

}
