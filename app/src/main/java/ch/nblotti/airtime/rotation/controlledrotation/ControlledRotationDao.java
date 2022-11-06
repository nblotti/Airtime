package ch.nblotti.airtime.rotation.controlledrotation;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ch.nblotti.airtime.EXERCICE_STATUS;
import ch.nblotti.airtime.sample.Sample;

@Dao
public interface ControlledRotationDao {

    @Query("SELECT * FROM ControlledRotation")
    LiveData<List<ControlledRotation>> getAll();


    @Query("UPDATE ControlledRotation SET air_time=:airTime WHERE uid = :uid")
    void updateAverageByIds(int uid, double airTime);


    @Query("UPDATE ControlledRotation SET status=:status WHERE uid = :uid")
    void updateStatus(long uid, EXERCICE_STATUS status);


    @Query("SELECT * FROM ControlledRotation WHERE session_id = :uid")
    LiveData<List<ControlledRotation>> loadAllByIds(long uid);


    @Query("DELETE  FROM ControlledRotation WHERE uid =(:uid)")
    void deleteById(long uid);


    @Query("SELECT * FROM ControlledRotation where uid =(:uid)")
    LiveData<ControlledRotation>  read(long uid);



    @Insert
    long[] insertAll(ControlledRotation... users);

    @Insert
    long insert(ControlledRotation user);

    @Delete
    void delete(ControlledRotation user);

}
