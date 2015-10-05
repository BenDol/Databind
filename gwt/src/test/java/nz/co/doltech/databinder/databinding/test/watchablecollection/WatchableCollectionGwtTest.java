package nz.co.doltech.databinder.databinding.test.watchablecollection;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;

import nz.co.doltech.databinder.util.Action1;
import nz.co.doltech.databinder.databinding.PlatformSpecificProvider;
import nz.co.doltech.databinder.databinding.gwt.Binder;
import nz.co.doltech.databinder.databinding.watchablecollection.Change;
import nz.co.doltech.databinder.databinding.watchablecollection.WatchableCollection;

public class WatchableCollectionGwtTest extends GWTTestCase {
    @Override
    public String getModuleName() {
        return "nz.co.doltech.databinder.databinding.DatabindingTest";
    }

    public void testA() {
        WatchableCollection<A> collection = new WatchableCollection<>();
        assertEquals(collection, collection);

        delayTestFinish(500);

        collection.addCallback(new Action1<List<Change>>() {
            @Override
            public void exec(List<Change> param) {
                finishTest();
            }
        });

        collection.add(new A());
    }

    public void testC() {
        A a = new A();

        PlatformSpecificProvider.get().setObjectMetadata(a, this);
        assertEquals(this, PlatformSpecificProvider.get().getObjectMetadata(a));
    }

    public void testB() {
        MyClassBundle bundle = GWT.create(MyClassBundle.class);
        bundle.register();

        A a = new A();
        A b = new A();

        Binder.bind(a, "value").to(b, "value");
        Binder.bind(a, "children").to(b, "children");

        a.setValue(55);
        b.setChildren(new WatchableCollection<A>());

        delayTestFinish(500);

        assertEquals(a.getChildren(), b.getChildren());
        a.getChildren().addCallback(new Action1<List<Change>>() {
            @Override
            public void exec(List<Change> param) {
                finishTest();
            }
        });

        b.addChild();

        assertEquals(a.getValue(), b.getValue());
        assertEquals(a.getValue(), 55);
    }
}
