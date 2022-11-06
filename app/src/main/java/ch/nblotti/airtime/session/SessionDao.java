package ch.nblotti.airtime.session;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SessionDao {

    @Query("SELECT * FROM session")
    LiveData<List<Session>> getAll();


    @Query("UPDATE session SET session_average=:sessionAverage WHERE uid = :uid")
    void updateAverageByIds(long uid, double sessionAverage);


    @Query("SELECT * FROM session WHERE uid IN (:uids)")
    LiveData<List<Session>> loadAllByIds(long[] uids);

    @Query("DELETE  FROM session WHERE uid =(:uid)")
    void deleteById(long uid);

    @Insert
    void insertAll(Session... users);

    @Delete
    void delete(Session user);

}
