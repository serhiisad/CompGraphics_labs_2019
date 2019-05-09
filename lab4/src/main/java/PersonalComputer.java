package main.java;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Cylinder;

import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.image.TextureLoader;
import javafx.scene.Scene;
//import javafx.scene.shape.Cylinder;

import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.media.j3d.Texture;
import javax.media.j3d.TextureAttributes;
import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import java.awt.*;

public class PersonalComputer {

    public static Box getBox(float length, float width, float height) {
        int primflags = Primitive.GENERATE_NORMALS + Primitive.GENERATE_TEXTURE_COORDS;
        //  return new Cone(radius, height, primflags, getPCAppearance());
        return new Box(length, width, height, primflags, PersonalComputer.getDesktopAppearance());
    }


    public static Cylinder getCylinder(float radius, float height) {
        int primflags = Primitive.GENERATE_NORMALS + Primitive.GENERATE_TEXTURE_COORDS;
        //  return new Cone(radius, height, primflags, getPCAppearance());
        return new Cylinder(radius, height, primflags, PersonalComputer.getDeskAppearance());
    }


    private static Appearance getAppearance(String texture_path) {

        //load
        TextureLoader loader = new TextureLoader(texture_path,
                "RGP", new Container());
        Texture texture = loader.getTexture();

        texture.setBoundaryModeS(Texture.WRAP);
        texture.setBoundaryModeT(Texture.WRAP);
        texture.setBoundaryColor(new Color4f(0.0f, 1.0f, 0.0f, 0.0f));
        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.MODULATE);

        Appearance ap = new Appearance();
        ap.setTexture(texture);
        ap.setTextureAttributes(texAttr);

        return ap;
    }

    public static Appearance getPeripheralsAppearance() {
        return getAppearance("resources/plastic.jpg");
    }


    public static Appearance getDisplayAppearance() {
        return getAppearance("resources/white_plastic.jpg");
    }

    public static Appearance getDeskAppearance() {
        return getAppearance("resources/metal.jpg");
    }

    private static Appearance getDesktopAppearance() {

        //load texture
        TextureLoader loader = new TextureLoader("resources/white_plastic.jpg", "LUMINANCE", new Container());
        Texture texture = loader.getTexture();
        //set texture boundary features
        texture.setBoundaryModeS(Texture.WRAP);
        texture.setBoundaryModeT(Texture.WRAP);
        texture.setBoundaryColor(new Color4f(0.0f, 1.0f, 1.0f, 0.0f));

        //set texture attributes
        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.MODULATE);

        Appearance ap = new Appearance();
        ap.setTexture(texture);
        ap.setTextureAttributes(texAttr);

//        Color3f emissive = new Color3f(0.0f, 0.05f, 0.0f);
//        Color3f emissive = new Color3f(Color.darkGray);
        Color3f emissive = new Color3f(Color.darkGray);
        Color3f ambient = new Color3f(Color.gray);
        Color3f diffuse = new Color3f(Color.lightGray);
        Color3f specular = new Color3f(Color.white);

//        Color3f ambient = new Color3f(0.2f, 0.5f, 0.15f);
//        Color3f diffuse = new Color3f(0.2f, 0.15f, .15f);
//        Color3f specular = new Color3f(0.0f, 0.8f, 0.0f);
        ap.setMaterial(new Material(ambient, emissive, diffuse, specular, 1.0f));
        return ap;
    }

}
