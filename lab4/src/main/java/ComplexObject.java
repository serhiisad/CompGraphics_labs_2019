package main.java;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.IOException;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Cylinder;
import main.java.PersonalComputer;
import com.sun.j3d.loaders.*;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.loaders.Scene; // Contains the object loaded from disk.
import com.sun.j3d.loaders.objectfile.ObjectFile; // Loader of .obj models
import com.sun.j3d.utils.universe.SimpleUniverse;

import javax.media.j3d.*;
import javax.swing.Timer;
import javax.vecmath.*;

/***
 * рисунок варіанту 19 - персональний комп'ютер
 */

public class ComplexObject implements ActionListener {
//public class ComplexObject{

    private TransformGroup PCTransformGroup;
    private Transform3D PCTransform3D = new Transform3D();
    private BranchGroup objRoot;
    private Timer timer;
    private float angle_x = 0;
    private float angle_y = 0;

    public static void main(String[] args) {
        new ComplexObject();
    }

    public ComplexObject() {
        timer = new Timer(50, this);
        timer.start();
        BranchGroup scene = createSceneGraph();
        SimpleUniverse u = new SimpleUniverse();
        u.getViewingPlatform().setNominalViewingTransform();
        u.addBranchGraph(scene);
    }

    private BranchGroup createSceneGraph() {


        // створюємо групу обєктів
        objRoot = new BranchGroup();

        // створюємо обєкт, що будемо додавати до групи
        PCTransformGroup = new TransformGroup();
        PCTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        buildPC();
        objRoot.addChild(PCTransformGroup);

        // налаштовуємо освітлення
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 200.0);
        Color3f light1Color = new Color3f(1.0f, 0.2f, 0.8f);
        Vector3f light1Direction = new Vector3f(4.0f, -7.0f, -12.0f);
        DirectionalLight light1 = new DirectionalLight(light1Color, light1Direction);
        light1.setInfluencingBounds(bounds);
        objRoot.addChild(light1);

        // встановлюємо навколишнє освітлення
        Color3f ambientColor = new Color3f(1.0f, 1.0f, 1.0f);
        AmbientLight ambientLightNode = new AmbientLight(ambientColor);
        ambientLightNode.setInfluencingBounds(bounds);
        objRoot.addChild(ambientLightNode);
        return objRoot;
    }

    public static Scene getSceneFromFile(String location) throws IOException {
        ObjectFile file = new ObjectFile(ObjectFile.RESIZE);
        return file.load(new FileReader(location));
    }

    private void buildPC() {


        //display
        //display
        TransformGroup tG_display = new TransformGroup();
        Transform3D t_display = new Transform3D();
        Scene display = null;
        try {
            display = getSceneFromFile("resources/models/Monitor.obj");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Vector3d vectorTop = new Vector3d(0.0, 0.0, 0.0);
        t_display.setTranslation(vectorTop);
        t_display.setScale(0.6);
        PCTransformGroup.setTransform(PCTransform3D);
        tG_display.setTransform(t_display);

        BranchGroup bg_display = display.getSceneGroup();
        Shape3D model0 = (Shape3D) bg_display.getChild(0);
        model0.setAppearance(PersonalComputer.getPeripheralsAppearance());
//        try{
        tG_display.addChild(display.getSceneGroup());
//        }
////        catch(NullPointerException e){
//            e.printStackTrace();
//        }
        PCTransformGroup.addChild(tG_display);

        //mouse
        TransformGroup tG_mouse = new TransformGroup();
        Transform3D t_mouse = new Transform3D();
        Scene mouse = null;
        try {
            mouse = getSceneFromFile("resources/models/Mouse.obj");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Vector3d vectorMiddle = new Vector3d(-0.65, -0.3, 0.35);
        t_mouse.setTranslation(vectorMiddle);
        t_mouse.setScale(0.1);
        PCTransformGroup.setTransform(PCTransform3D);
        tG_mouse.setTransform(t_mouse);

        BranchGroup bg_mouse = mouse.getSceneGroup();
        Shape3D model = (Shape3D) bg_mouse.getChild(0);
        model.setAppearance(PersonalComputer.getPeripheralsAppearance());
//        try{
        tG_mouse.addChild(bg_mouse);
//        tG_mouse.addChild(model);
//        }
//        catch(NullPointerException e){
//            e.printStackTrace();
//        }
        PCTransformGroup.addChild(tG_mouse);

        //keyboard
        TransformGroup tG_keyboard = new TransformGroup();
        Transform3D t_keyboard = new Transform3D();
        Scene keyboard = null;
        try {
            keyboard = getSceneFromFile("resources/models/keyboard.obj");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Vector3d vectorBottom = new Vector3d(0, -0.31, 0.35);
        t_keyboard.setTranslation(vectorBottom);
        t_keyboard.setScale(0.4);
        PCTransformGroup.setTransform(PCTransform3D);
        tG_keyboard.setTransform(t_keyboard);

        BranchGroup bg_keyboard = keyboard.getSceneGroup();
        Shape3D model2 = (Shape3D) bg_keyboard.getChild(0);
        model2.setAppearance(PersonalComputer.getPeripheralsAppearance());
        //try{
        tG_keyboard.addChild(bg_keyboard);
//        }
//        catch (NullPointerException e){
//            e.printStackTrace();
//        }
        PCTransformGroup.addChild(tG_keyboard);


        //desk
        TransformGroup tG_desk = new TransformGroup();
        Transform3D t_desk = new Transform3D();
        //desk lid
        Box deskTop = PersonalComputer.getBox(0.75f, 0.01f, 0.65f);
        Vector3d vector_desk = new Vector3d(-0.1, -0.35, 0.25);
        t_desk.setTranslation(vector_desk);

        tG_desk.setTransform(t_desk);
        tG_desk.addChild(deskTop);
        PCTransformGroup.addChild(tG_desk);

        //leg

        TransformGroup tG_legs = new TransformGroup();
        Transform3D t_legs = new Transform3D();

        Cylinder desk_leg1 = PersonalComputer.getCylinder(0.02f, 0.7f);
        Vector3d vector_leg1 = new Vector3d(0, -0.7, 0.3);
        t_legs.setTranslation(vector_leg1);

        tG_legs.setTransform(t_legs);
        tG_legs.addChild(desk_leg1);

        PCTransformGroup.addChild(tG_legs);


        //stand

        TransformGroup tG_stand = new TransformGroup();
        Transform3D t_stand = new Transform3D();

        Box desk_stand1 = PersonalComputer.getBox(0.2f, 0.01f, 0.2f);
        Vector3d vector_stand = new Vector3d(0, -1, 0.3);
        t_stand.setTranslation(vector_stand);

        tG_stand.setTransform(t_stand);
        tG_stand.addChild(desk_stand1);

        PCTransformGroup.addChild(tG_stand);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        PCTransform3D.rotY(angle_y);

        PCTransformGroup.setTransform(PCTransform3D);
        angle_y += 0.01;

//        PCTransform3D.rotX(angle_x);
//        PCTransformGroup.setTransform(PCTransform3D);
//        angle_x += 0.02;

    }

}

    
