/**
 * AET
 *
 * Copyright (C) 2013 Cognifide Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cognifide.aet.runner.processing.data;

import static com.google.common.base.Strings.isNullOrEmpty;

import com.cognifide.aet.communication.api.metadata.Suite;
import com.cognifide.aet.communication.api.metadata.ValidatorException;
import com.cognifide.aet.vs.MetadataDAO;
import com.cognifide.aet.vs.SimpleDBKey;
import com.cognifide.aet.vs.StorageException;
import java.util.Optional;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("ALL")//todo Delete beffore pullrequest
@Component(service = SuiteDataService.class)
public class SuiteDataService {

  private static final Logger LOGGER = LoggerFactory.getLogger(SuiteDataService.class);

  @Reference
  private MetadataDAO metadataDAO;

  /**
   * @param currentRun - current suite run
   * @return suite wrapper with all patterns from the last or specified (see Suite.patternCorrelationId) run, if this is the first run of the suite, patterns will be empty.
   */
  public Suite enrichWithPatterns(final Suite currentRun) throws StorageException {
    final SimpleDBKey dbKey = new SimpleDBKey(currentRun);
    Suite lastVersion = metadataDAO.getLatestRun(dbKey, currentRun.getName());
    String checkSumCurrentRunProject = currentRun.getCheckSum();
    final Suite pattern;

    if (isTestRunWithCheckSum(checkSumCurrentRunProject)) {
      Optional<Suite> suiteByChecksum = Optional.ofNullable(metadataDAO.getSuiteByChecksum(dbKey, checkSumCurrentRunProject));
      pattern = suiteByChecksum.orElse(assignCheckSumToPattern(dbKey, currentRun, lastVersion, checkSumCurrentRunProject));
    } else {
      if (currentRun.getPatternCorrelationId() == null) {
        pattern = lastVersion;
      } else {
        pattern = metadataDAO.getSuite(dbKey, currentRun.getPatternCorrelationId());
      }
    }
    return SuiteMergeStrategy.merge(currentRun, lastVersion, pattern);
  }

  private Suite assignCheckSumToPattern(SimpleDBKey dbKey, Suite currentRun, Suite lastVersion, String checkSumCurrentRunProject) throws StorageException {
    Suite pattern;
    if (isSuitHasPattern(currentRun)) {
      pattern = metadataDAO.getSuite(dbKey, currentRun.getPatternCorrelationId());
    } else {
      pattern = lastVersion;
    }
    if (pattern != null) {
      pattern.setCheckSumProject(checkSumCurrentRunProject);
      updateSuit(pattern);
    }
    return pattern;
  }

  private boolean isSuitHasPattern(Suite currentRun) {
    return !isNullOrEmpty(currentRun.getPatternCorrelationId());
  }

  private boolean isChecksumIsAssignedToPathern(Optional<Suite> suiteByChecksum) {
    return suiteByChecksum.isPresent();
  }

  private boolean isTestRunWithCheckSum(String checkSumCurrentRunProject) {
    return !isNullOrEmpty(checkSumCurrentRunProject);
  }

  private void updateSuit(Suite currentRun) throws StorageException {
    try {
      metadataDAO.updateSuite(currentRun);
    } catch (ValidatorException e) {
      LOGGER.error("Invalid json provided by client: {}", e.getIssues(), e);
    }
  }

  public Suite saveSuite(final Suite suite) throws ValidatorException, StorageException {
    return metadataDAO.saveSuite(suite);
  }
}
