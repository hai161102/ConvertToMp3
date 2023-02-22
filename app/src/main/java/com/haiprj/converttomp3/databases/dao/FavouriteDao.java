package com.haiprj.converttomp3.databases.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.haiprj.converttomp3.models.FileModel;

import java.util.List;

@Dao
public interface FavouriteDao {
    @Query("Select * from file_model")
    List<FileModel> getAllFavourite();

    @Query("Select * from file_model where id =:index")
    List<FileModel> getFavouriteAt(int index);

    @Insert
    void insert(FileModel... baseModels);

    @Query("Delete from file_model where id =:index")
    void delete(int index);

    @Query("Delete from file_model")
    void deleteAll();

}
