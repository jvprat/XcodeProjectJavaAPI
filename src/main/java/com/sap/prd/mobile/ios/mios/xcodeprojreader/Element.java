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
package com.sap.prd.mobile.ios.mios.xcodeprojreader;

public abstract class Element
{
  private final ProjectFile projectFile;
  private final Dict dict;

  protected Element(ProjectFile projectFile, Dict dict)
  {
    this.projectFile = projectFile;
    this.dict = dict;
  }

  protected ProjectFile getProjectFile()
  {
    return projectFile;
  }

  public Dict getDict()
  {
    return dict;
  }

  public String getIsA()
  {
    return getDict().getString("isa");
  }
}
