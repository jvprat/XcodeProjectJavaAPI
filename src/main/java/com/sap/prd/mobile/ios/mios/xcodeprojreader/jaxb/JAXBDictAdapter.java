/*
 * #%L
 * xcode-project-reader
 * %%
 * Copyright (C) 2012 SAP AG
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.sap.prd.mobile.ios.mios.xcodeprojreader.jaxb;

import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.sap.prd.mobile.ios.mios.xcodeprojreader.Array;
import com.sap.prd.mobile.ios.mios.xcodeprojreader.Dict;

public class JAXBDictAdapter extends XmlAdapter<JAXBDict, Dict>
{
  @Override
  public JAXBDict marshal(Dict dict) throws Exception
  {
    JAXBDict jaxbDict = new JAXBDict();
    Array elements = new JAXBPlist().createArray();
    for (Map.Entry<String, Object> entry : dict.entrySet())
    {
      JAXBKey key = new JAXBKey();
      key.setValue(entry.getKey());
      elements.add(key);

      Object value = entry.getValue();
      if (value instanceof Dict)
      {
        value = marshal((Dict) value);
      }
      else if (value instanceof Array)
      {
        value = new JAXBArrayAdapter().marshal((Array) value);
      }
      else if (value instanceof Boolean)
      {
        value = ((Boolean)value) ? new JAXBTrue() : new JAXBFalse();
      }
      elements.add(value);
    }
    jaxbDict.setElements(elements);
    return jaxbDict;
  }

  @Override
  public Dict unmarshal(JAXBDict jaxbDict) throws Exception
  {
    Dict dict = new LinkedHashMapDict();
    for (int i = 0; i < jaxbDict.getElements().size(); i += 2)
    {
      Object key = jaxbDict.getElements().get(i);
      if (key instanceof JAXBKey)
      {
        key = ((JAXBKey) key).getValue();
      }

      Object value = jaxbDict.getElements().get(i + 1);
      if (value instanceof JAXBDict)
      {
        value = unmarshal((JAXBDict) value);
      }
      else if (value instanceof JAXBArray)
      {
        value = new JAXBArrayAdapter().unmarshal((JAXBArray) value);
      }
      else if (value instanceof JAXBTrue)
      {
        value = Boolean.TRUE;
      }
      else if (value instanceof JAXBFalse)
      {
        value = Boolean.FALSE;
      }
      dict.put((String) key, value);
    }
    return dict;
  }
}