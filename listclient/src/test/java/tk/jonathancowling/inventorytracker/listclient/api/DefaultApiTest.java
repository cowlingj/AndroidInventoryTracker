package tk.jonathancowling.inventorytracker.listclient.api;

import tk.jonathancowling.inventorytracker.listclient.ApiClient;
import tk.jonathancowling.inventorytracker.listclient.model.Item;
import tk.jonathancowling.inventorytracker.listclient.model.ItemPrototype;
import tk.jonathancowling.inventorytracker.listclient.model.PartialItem;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for DefaultApi
 */
public class DefaultApiTest {

    private DefaultApi api;

    @Before
    public void setup() {
        api = new ApiClient().createService(DefaultApi.class);
    }

    /**
     * 
     *
     * removes an item in the list
     */
    @Test
    public void listDeleteTest() {
        Integer item = null;
        // api.listDelete(item);

        // TODO: test validations
    }
    /**
     * 
     *
     * returns items in the list
     */
    @Test
    public void listGetTest() {
        String from = null;
        Integer limit = null;
        // List<Item> response = api.listGet(from, limit);

        // TODO: test validations
    }
    /**
     * 
     *
     * adds an item to the list
     */
    @Test
    public void listPostTest() {
        ItemPrototype itemPrototype = null;
        // List<Item> response = api.listPost(itemPrototype);

        // TODO: test validations
    }
    /**
     * 
     *
     * replaces an item in the list
     */
    @Test
    public void listPutTest() {
        PartialItem partialItem = null;
        // List<Item> response = api.listPut(partialItem);

        // TODO: test validations
    }
    /**
     * 
     *
     * returns a report on the list over a given period
     */
    @Test
    public void reportGetTest() {
        // api.reportGet();

        // TODO: test validations
    }
}
