package com.example.shopping_cart.util;

import com.example.shopping_cart.entity.CatEntity;
import com.example.shopping_cart.request_dto.CatDTOResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomizeDateFormat {

    public static String formatTimestamp(Date dt) {

        if (dt == null) {

            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return sdf.format(dt);
    }
}