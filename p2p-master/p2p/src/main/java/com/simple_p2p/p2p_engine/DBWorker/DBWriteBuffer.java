package com.simple_p2p.p2p_engine.DBWorker;

import com.simple_p2p.entity.EntityUniqueConstrain;
import io.netty.util.internal.ConcurrentSet;
import org.springframework.stereotype.Component;

import java.util.Collection;



@Component
public class DBWriteBuffer {

    private ConcurrentSet<EntityUniqueConstrain> bufferSetWriteToDB = new ConcurrentSet<>();

    private DBWriteBuffer() {
    }

    public static class SettingsHolder {
        public static final DBWriteBuffer HOLDER_INSTANCE = new DBWriteBuffer();
    }

    public static DBWriteBuffer getInstance() {
        return DBWriteBuffer.SettingsHolder.HOLDER_INSTANCE;
    }

    public void addEntityToDB(EntityUniqueConstrain entity){
        bufferSetWriteToDB.add(entity);
    }

    public void addAllEntityToDB (Collection entityCollection){
        bufferSetWriteToDB.addAll(entityCollection);
    }

    public ConcurrentSet<EntityUniqueConstrain> getBufferSetWriteToDB() {
        ConcurrentSet<EntityUniqueConstrain>  tempConcurrentSet = new ConcurrentSet<>();
        tempConcurrentSet.addAll(bufferSetWriteToDB);
        bufferSetWriteToDB.clear();
        return tempConcurrentSet;
    }

    public boolean isEmpty(){
        return bufferSetWriteToDB.isEmpty();
    }
}
