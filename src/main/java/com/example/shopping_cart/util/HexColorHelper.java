package com.example.shopping_cart.util;

import com.itextpdf.text.BaseColor;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class HexColorHelper {

    public static BaseColor hexToBaseColor(String hex) {
        Color awtColor = Color.decode(hex); // java.awt.Color
        return new BaseColor(awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue());
    }
}