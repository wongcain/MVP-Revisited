package com.cainwong.mvprevisited.core.places;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import rx.observers.TestSubscriber;
import rx.subscriptions.CompositeSubscription;

import static org.assertj.core.api.Assertions.assertThat;

public class PlaceManagerTest {

    PlaceManager mPlaceManager;
    Place place1 = new Place1();
    Place place2 = new Place2("test");
    Place place3 = new Place3();
    CompositeSubscription mSubscriptions = new CompositeSubscription();

    @Before
    public void setup() throws Exception {
        mPlaceManager = new PlaceManager();
    }

    @After
    public void cleanup() throws Exception {
        mSubscriptions.clear();
    }

    @Test
    public void testGlobalSubscription() throws Exception {
        TestSubscriber<Place> testSubscriber = TestSubscriber.create();
        mSubscriptions.add(mPlaceManager.onGotoPlaceGlobal().subscribe(testSubscriber));
        mPlaceManager.gotoPlace(place1);
        mPlaceManager.gotoPlace(place2);
        mPlaceManager.gotoPlace(place3);
        testSubscriber.assertValues(place1, place2, place3);
    }

    @Test
    public void testFilteredSubscription() throws Exception {
        TestSubscriber<Place> testSubscriber = TestSubscriber.create();
        mSubscriptions.add(mPlaceManager.onGotoPlace(Place2.class).subscribe(testSubscriber));
        mPlaceManager.gotoPlace(place1);
        mPlaceManager.gotoPlace(place2);
        mPlaceManager.gotoPlace(place3);
        testSubscriber.assertValues(place2);
    }

    @Test
    public void testDescendentsSubscription() throws Exception {
        TestSubscriber<Place> testSubscriber = TestSubscriber.create();
        mSubscriptions.add(mPlaceManager.onGotoPlaceOrDescendants(Place2.class).subscribe(testSubscriber));
        mPlaceManager.gotoPlace(place1);
        mPlaceManager.gotoPlace(place2);
        mPlaceManager.gotoPlace(place3);
        testSubscriber.assertValues(place2, place3);
    }

    @Test
    public void testGoBack() throws Exception {
        TestSubscriber<Place> testSubscriber = TestSubscriber.create();
        mSubscriptions.add(mPlaceManager.onGotoPlaceGlobal().subscribe(testSubscriber));
        mPlaceManager.gotoPlace(place1);
        mPlaceManager.gotoPlace(place2);
        mPlaceManager.gotoPlace(place3);
        assertThat(mPlaceManager.goBack()).isTrue();
        assertThat(mPlaceManager.goBack()).isTrue();
        assertThat(mPlaceManager.goBack()).isFalse();
        testSubscriber.assertValues(place1, place2, place3, place2, place1);

    }

    @Test
    public void testHistoryActionAdd() throws Exception {
        mPlaceManager.gotoPlace(place1);
        mPlaceManager.gotoPlace(place1);
        mPlaceManager.gotoPlace(place2);
        mPlaceManager.gotoPlace(place3);
        assertThat(mPlaceManager.getCurrentPlace()).isEqualTo(place3);
        assertThat(mPlaceManager.goBack()).isTrue();
        assertThat(mPlaceManager.getCurrentPlace()).isEqualTo(place2);
        assertThat(mPlaceManager.goBack()).isTrue();
        assertThat(mPlaceManager.getCurrentPlace()).isEqualTo(place1);
        assertThat(mPlaceManager.goBack()).isTrue();
        assertThat(mPlaceManager.getCurrentPlace()).isEqualTo(place1);
        assertThat(mPlaceManager.goBack()).isFalse();
    }

    @Test
    public void testHistoryActionReplaceTop() throws Exception {
        mPlaceManager.gotoPlace(place1, PlaceManager.HistoryAction.REPLACE_TOP);
        mPlaceManager.gotoPlace(place1, PlaceManager.HistoryAction.REPLACE_TOP);
        mPlaceManager.gotoPlace(place2, PlaceManager.HistoryAction.REPLACE_TOP);
        mPlaceManager.gotoPlace(place3, PlaceManager.HistoryAction.REPLACE_TOP);
        assertThat(mPlaceManager.getCurrentPlace()).isEqualTo(place3);
        assertThat(mPlaceManager.goBack()).isFalse();
        assertThat(mPlaceManager.getCurrentPlace()).isEqualTo(place3);
    }

    @Test
    public void testHistoryActionTryBackToSameType() throws Exception {
        mPlaceManager.gotoPlace(place1, PlaceManager.HistoryAction.TRY_BACK_TO_SAME_TYPE);
        mPlaceManager.gotoPlace(place2, PlaceManager.HistoryAction.TRY_BACK_TO_SAME_TYPE);
        mPlaceManager.gotoPlace(place3, PlaceManager.HistoryAction.TRY_BACK_TO_SAME_TYPE);
        assertThat(mPlaceManager.getCurrentPlace()).isEqualTo(place3);
        assertThat(mPlaceManager.goBack()).isTrue();
        assertThat(mPlaceManager.getCurrentPlace()).isEqualTo(place2);
        mPlaceManager.gotoPlace(place1, PlaceManager.HistoryAction.TRY_BACK_TO_SAME_TYPE);
        assertThat(mPlaceManager.goBack()).isFalse();
        assertThat(mPlaceManager.getCurrentPlace()).isEqualTo(place1);
    }

    @Test
    public void testHistoryActionTryBackToEqual() throws Exception {
        Place place2a = new Place2("Another place");
        mPlaceManager.gotoPlace(place1, PlaceManager.HistoryAction.TRY_BACK_TO_EQUAL);
        mPlaceManager.gotoPlace(place2, PlaceManager.HistoryAction.TRY_BACK_TO_EQUAL);
        mPlaceManager.gotoPlace(place3, PlaceManager.HistoryAction.TRY_BACK_TO_EQUAL);
        mPlaceManager.gotoPlace(place2a, PlaceManager.HistoryAction.TRY_BACK_TO_EQUAL);
        assertThat(mPlaceManager.getCurrentPlace()).isEqualTo(place2a);
        assertThat(mPlaceManager.goBack()).isTrue();
        assertThat(mPlaceManager.getCurrentPlace()).isEqualTo(place3);
        mPlaceManager.gotoPlace(place2, PlaceManager.HistoryAction.TRY_BACK_TO_EQUAL);
        assertThat(mPlaceManager.getCurrentPlace()).isEqualTo(place2);
        assertThat(mPlaceManager.goBack()).isTrue();
        assertThat(mPlaceManager.getCurrentPlace()).isEqualTo(place1);
    }

    class Place1 extends SimplePlace {}

    @PlaceConfig(parent = Place1.class)
    class Place2 extends Place<String> {
        public Place2(String mData) {
            super(mData);
        }
    }

    @PlaceConfig(parent = Place2.class)
    class Place3 extends SimplePlace {}

}
