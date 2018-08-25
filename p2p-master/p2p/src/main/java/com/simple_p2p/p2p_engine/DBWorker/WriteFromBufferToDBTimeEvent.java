package com.simple_p2p.p2p_engine.DBWorker;

import com.simple_p2p.entity.EntityUniqueConstrain;
import com.simple_p2p.p2p_engine.server.Settings;
import io.netty.util.internal.ConcurrentSet;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

//Class need to update sqlite from single thread and update unique fields without exceptions.

public class WriteFromBufferToDBTimeEvent extends TimerTask{

    private DBWriteBuffer dbWriteBuffer = DBWriteBuffer.getInstance();
    private ConcurrentSet<EntityUniqueConstrain> concurrentSet = new ConcurrentSet();
    private Settings settings = Settings.getInstance();
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void run() {
        if(!dbWriteBuffer.isEmpty()){
            //logger.info("try DB update");
            concurrentSet.addAll(dbWriteBuffer.getBufferSetWriteToDB());
            for(EntityUniqueConstrain entity:concurrentSet){
                Session session = settings.getSessionFactory().openSession();
                if(ifNotExists(entity,session)){
                    session.beginTransaction();
                    //logger.info("update");
                    session.save(entity);
                    session.getTransaction().commit();
                }
                session.close();
            }
            logger.info("End updating DB");
        }
    }

    public Boolean ifNotExists (EntityUniqueConstrain entity, Session session) {
        Query query = session.createQuery("select count(*) from "+entity.getClass().getSimpleName()+" where "+entity.getUniqueConstrainName()+"= :unique");
        query.setParameter("unique", String.valueOf(entity.getUniqueConstrainValue()) );
        Long result = (Long)query.uniqueResult();
        return (result<1);
    }
}
