package ch.nblotti.airtime.sample;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ch.nblotti.airtime.session.Session;

@Dao
public interface SampleDao {

    @Query("SELECT * FROM sample")
    LiveData<List<Sample>> getAll();

    @Query("SELECT * FROM sample WHERE session_id = :uid")
    LiveData<List<Sample>> loadAllByIds(long uid);

    @Query("SELECT * FROM sample WHERE session_id = :uid")
    List<Sample> loadAllByIdsSync(long uid);

    @Query("DELETE  FROM sample WHERE uid =(:uid)")
    void deleteById(long uid);

    @Query("DELETE  FROM sample WHERE session_id =(:session_id)")
    void deleteBySessionId(long session_id);


    @Insert
    void insertAll(Sample... sample);

    @Delete
    void delete(Sample sample);

}
