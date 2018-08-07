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
package com.cognifide.aet.worker.drivers;

import com.cognifide.aet.job.api.collector.ProxyServerWrapper;
import com.cognifide.aet.worker.exceptions.WorkerException;
import java.net.UnknownHostException;
import java.util.Map;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.openqa.selenium.Proxy;

/**
 * Some constants and utils used by web driver factory implementations.
 */
public final class WebDriverHelper {

  public static final String NAME_LABEL = "Name";

  public static final String NAME_DESC = "Driver name that will be used by other services like Screenshot Collectors";

  public static final String PATH_LABEL = "Custom path to Firefox binary";

  public static final String LOG_FILE_PATH_LABEL = "Path to firefox error log";

  public static final String SELENIUM_GRID_URL_LABEL = "Selenium grid URL";

  public static final String SELENIUM_GRID_URL_DESC = "Url to selenium grid hub. When null local Chrome driver will be used. Local Chrome driver does not work on Linux";

  public static final String DEFAULT_SELENIUM_GRID_URL = "http://localhost:4444/wd/hub";

  private WebDriverHelper() {
    // restrict instantiation
  }

  public static String getProp(Map<String, String> properties, String name, String defaultValue) {
    return PropertiesUtil.toString(properties.get(name), defaultValue);
  }

  public static Proxy setupProxy(ProxyServerWrapper proxyServer) throws WorkerException {
    proxyServer.setCaptureContent(true);
    proxyServer.setCaptureHeaders(true);
    try {
      return proxyServer.seleniumProxy();
    } catch (UnknownHostException e) {
      throw new WorkerException(e.getMessage(), e);
    }
  }
}
