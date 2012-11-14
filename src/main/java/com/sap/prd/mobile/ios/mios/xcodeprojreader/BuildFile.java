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

import com.sap.prd.mobile.ios.mios.xcodeprojreader.treeelements.PBXFileReference;

public class BuildFile extends Element
{
  public BuildFile(ProjectFile projectFile)
  {
    this(projectFile, projectFile.createDict());
  }

  public BuildFile(ProjectFile projectFile, Dict dict)
  {
    super(projectFile, dict);
  }

  public PBXFileReference getFile()
  {
    String fileRef = getDict().getString("fileRef");
    Dict file = getProjectFile().getObjectByReference(fileRef);
    return new PBXFileReference(getProjectFile(), file);
  }
}
