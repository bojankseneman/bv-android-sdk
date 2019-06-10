package com.bazaarvoice.bvandroidsdk;

import android.os.AsyncTask;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadows.ShadowLegacyAsyncTask;

public class BvSdkShadows {
  @Implements(AdIdRequestTask.class)
  public static class BvShadowAsyncTask<Params, Progress, Result> extends ShadowLegacyAsyncTask<Params, Progress, Result> {
    @Implementation
    public AsyncTask<Params, Progress, Result> execute(Params... params) {
      return super.execute(params);
    }
  }
}
