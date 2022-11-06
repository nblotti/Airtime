package ch.nblotti.airtime.ropejump;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ch.nblotti.airtime.EXERCICE_STATUS;

@Dao
public interface RopeJumpDao {

    @Query("SELECT * FROM RopeJump")
    LiveData<List<RopeJump>> getAll();


    @Query("UPDATE RopeJump SET number=:number WHERE uid = :uid")
    void updateNumberByIds(int uid, int number);


    @Query("UPDATE RopeJump SET status=:status WHERE uid = :uid")
    void updateStatus(long uid, EXERCICE_STATUS status);


    @Query("SELECT * FROM RopeJump WHERE session_id = :uid and type = :ropejump_type")
    LiveData<List<RopeJump>> loadAllByIdsAndType(long uid, ROPEJUMP_TYPE ropejump_type);


    @Query("DELETE  FROM RopeJump WHERE uid =(:uid)")
    void deleteById(long uid);


    @Query("SELECT * FROM RopeJump where uid =(:uid)")
    LiveData<RopeJump> read(long uid);


    @Insert
    long[] insertAll(RopeJump... users);

    @Insert
    long insert(RopeJump user);

    @Delete
    void delete(RopeJump user);

}
