package com.jee.boot.alone.license.client.manager;

import de.schlichtherle.license.LicenseManager;
import de.schlichtherle.license.LicenseParam;

/**
 * license管理类
 *
 * @author jeeLearner
 * @version V1.0
 */
public class LicenseManagerHolder {

    private static ClientLicenseManager licenseManager;

    public static synchronized ClientLicenseManager getInstance(LicenseParam licenseParam){
        if (licenseManager == null){
            try {
                licenseManager = new ClientLicenseManager(licenseParam);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return licenseManager;
    }
}

