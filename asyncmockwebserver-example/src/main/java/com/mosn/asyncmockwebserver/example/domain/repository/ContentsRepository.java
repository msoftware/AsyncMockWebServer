package com.mosn.asyncmockwebserver.example.domain.repository;

import com.mosn.asyncmockwebserver.example.domain.model.Contents;
import com.mosn.asyncmockwebserver.example.domain.repository.callback.NetworkCallback;

public interface ContentsRepository {
    void search(String keyword, NetworkCallback<Contents> callback);
}
