/**
 * AET
 *
 * Copyright (C) 2013 Cognifide Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.cognifide.aet.runner.processing.data.wrappers;

import com.cognifide.aet.communication.api.metadata.RunType;
import com.cognifide.aet.communication.api.wrappers.Run;

public class RunIndexWrapperFactory {

  @SuppressWarnings("unchecked")
  public static RunIndexWrapper createInstance(Run objectToRunWrapper) {
    if(objectToRunWrapper.getType() == RunType.SUITE){
      return new SuiteRunIndexWrapper(objectToRunWrapper);
    } else if (objectToRunWrapper.getType() == RunType.TEST){
      return new TestRunIndexWrapper(objectToRunWrapper);
    } else if (objectToRunWrapper.getType() == RunType.URL){
      return new UrlRunIndexWrapper(objectToRunWrapper);
    }
    return null;
  }

}
