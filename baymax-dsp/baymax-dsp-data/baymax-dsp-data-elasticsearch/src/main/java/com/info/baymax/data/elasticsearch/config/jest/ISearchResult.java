package com.info.baymax.data.elasticsearch.config.jest;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.searchbox.core.SearchResult;

// fix jest issues: "https://github.com/searchbox-io/Jest/issues/656"
public class ISearchResult extends SearchResult {

    public ISearchResult(SearchResult searchResult) {
        super(searchResult);
    }

    @Override
    public Long getTotal() {
        Long total = null;
        JsonElement element = getPath(PATH_TO_TOTAL);
        if (element != null) {
            if (element instanceof JsonPrimitive) {
                return ((JsonPrimitive) element).getAsLong();
            } else if (element instanceof JsonObject) {
                total = ((JsonObject) element).getAsJsonPrimitive("value").getAsLong();
            }
        }
        return total;
    }
}
