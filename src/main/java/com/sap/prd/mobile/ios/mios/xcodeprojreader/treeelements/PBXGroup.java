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
package com.sap.prd.mobile.ios.mios.xcodeprojreader.treeelements;

import com.sap.prd.mobile.ios.mios.xcodeprojreader.Dict;
import com.sap.prd.mobile.ios.mios.xcodeprojreader.ElementFactory;
import com.sap.prd.mobile.ios.mios.xcodeprojreader.ProjectFile;
import com.sap.prd.mobile.ios.mios.xcodeprojreader.ReferenceArray;

public class PBXGroup extends TreeElement {

  public PBXGroup(ProjectFile projectFile)
  {
    this(projectFile, projectFile.createDict());
  }

  public PBXGroup(ProjectFile projectFile, Dict dict)
  {
    super(projectFile, dict);
  }

  public ReferenceArray<TreeElement> getChildren()
  {
    return new ReferenceArray<TreeElement>(getProjectFile(), getDict().getOrCreateAndSetArray("children"),
          new ElementFactory<TreeElement>() {
            @Override
            public TreeElement create(ProjectFile projectFile, Dict dict) {
              return TreeElement.create(projectFile, dict);
            }
          });
  }
}
