package com.bazaarvoice.bvandroidsdk;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(shadows = {BaseShadows.ShadowAdIdClientWithLimit.class, BaseShadows.ShadowNetwork.class})
public class BvAdIdLimitTest {
  private BvStubData stubData;

  @Before
  public void setup() {
    stubData = new BvStubData();
  }

  @Test
  public void shouldReturnNonTracking() throws Exception {
    String actualAdId = stubData.getAdIdFuture(RuntimeEnvironment.application).get();
    assertEquals(BVEventValues.NONTRACKING_TOKEN, actualAdId);
  }

  @Test
  public void shouldReturnAdInfo() {
    BVAdvertisingId.BvAdInfoFetcher fetcher = new BVAdvertisingId.BvAdInfoFetcher(RuntimeEnvironment.application);
    AdvertisingIdClient.Info info = fetcher.getAdInfo();
    assertNotNull(info);
  }
}
