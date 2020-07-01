package com.info.baymax.dsp.access.dataapi.data.elasticsearch;

import com.google.gson.JsonObject;
import io.searchbox.core.SearchResult;

// fix jest issues: "https://github.com/searchbox-io/Jest/issues/656"
public class ISearchResult extends SearchResult {

    public ISearchResult(SearchResult searchResult) {
        super(searchResult);
    }

    @Override
    public Long getTotal() {
        Long total = null;
        JsonObject obj = (JsonObject) getPath(PATH_TO_TOTAL);
        if (obj != null)
            total = obj.getAsJsonPrimitive("value").getAsLong();
        return total;
    }

    @Override
    public Float getMaxScore() {
        Float maxScore = null;
        JsonObject obj = (JsonObject) getPath(PATH_TO_MAX_SCORE);
        if (obj != null && !obj.isJsonNull())
            maxScore = obj.getAsJsonPrimitive("value").getAsFloat();
        return maxScore;
    }
}
