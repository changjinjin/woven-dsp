package com.info.baymax.dsp.access.dataapi.data;

/**
 * DataReader抽象
 *
 * @author jingwei.yang
 * @date 2020年6月22日 上午10:53:48
 */
public abstract class MapEntityDataReader implements DataReader<MapEntity, MapEntity> {

    private Engine engine;

    protected MapEntityDataReader(Engine engine) {
        this.engine = engine;
    }

    @Override
    public boolean supports(StorageConf conf) {
        return this.engine.equals(conf.getEngine());
    }
}
