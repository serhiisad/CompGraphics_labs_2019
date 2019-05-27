package com.task;

import com.sun.j3d.utils.universe.*;

import javax.media.j3d.*;
import javax.vecmath.*;
import javax.media.j3d.Background;

import com.sun.j3d.loaders.*;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.image.TextureLoader;

import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import javax.swing.JFrame;

public class Car extends JFrame {
	private static Canvas3D canvas;
	private static SimpleUniverse universe;
	private static BranchGroup root;
	
	private static TransformGroup car;
	
    public Car() throws IOException {
    	configureWindow();
        configureCanvas();
        configureUniverse();
        
        root = new BranchGroup();

        addImageBackground();
        
        addDirectionalLightToUniverse();
        addAmbientLightToUniverse();
        
        ChangeViewAngle();

        car = getCarGroup();
        root.addChild(car);
        
        root.compile();
        universe.addBranchGraph(root);
    }
    
    
    // start initialization
    
    private void configureWindow() {
        setTitle("Car on the road");
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    private void configureCanvas() {
    	canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        canvas.setDoubleBufferEnable(true);
        getContentPane().add(canvas, BorderLayout.CENTER);
    }
    
    private void configureUniverse() {
        universe = new SimpleUniverse(canvas);
        universe.getViewingPlatform().setNominalViewingTransform();
    }
    
    private void addImageBackground() {
        TextureLoader t = new TextureLoader("source_folder/old_road.jpg", canvas);
        Background background = new Background(t.getImage());
        background.setImageScaleMode(Background.SCALE_FIT_ALL);
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        background.setApplicationBounds(bounds);
        root.addChild(background);
    }
    
    private void addDirectionalLightToUniverse() {
	    BoundingSphere bounds = new BoundingSphere();
	    bounds.setRadius(100);

	    DirectionalLight light = new DirectionalLight(new Color3f(1, 1, 1), new Vector3f(-1, -1, -1));
	    light.setInfluencingBounds(bounds);

	    root.addChild(light);
	}
    
    private void addAmbientLightToUniverse() {
        AmbientLight light = new AmbientLight(new Color3f(1, 1, 1));
        light.setInfluencingBounds(new BoundingSphere());
        root.addChild(light);
    }
    
    private void ChangeViewAngle() {
        ViewingPlatform vp = universe.getViewingPlatform();
        TransformGroup vpGroup = vp.getMultiTransformGroup().getTransformGroup(0);
        Transform3D vpTranslation = new Transform3D();
        vpTranslation.setTranslation(new Vector3f(0, 0, 6));
        vpGroup.setTransform(vpTranslation);
    }
    
    // end initialization

    // start main
    private TransformGroup getCarGroup() throws IOException {
        // Shape3D shape = getModelShape3D("frame0", "source_folder/car/Car.obj");
    	Shape3D shape = getModelShape3D("platinum1", "source_folder/car/Car.obj");
        Shape3D shape1 = getModelShape3D("wheel4", "source_folder/car/Car.obj");
        Shape3D shape2 = getModelShape3D("wheel3", "source_folder/car/Car.obj");
        Shape3D shape3 = getModelShape3D("wheel2", "source_folder/car/Car.obj");
        Shape3D shape4 = getModelShape3D("wheel1", "source_folder/car/Car.obj");

        String casing_path = "source_folder/textures/zhest.jpg";
        String wheels_path = "source_folder/textures/rubber.jpg";

        addAppearance(shape, casing_path);
        addAppearance(shape1, wheels_path);
        addAppearance(shape2, wheels_path);
        addAppearance(shape3, wheels_path);
        addAppearance(shape4, wheels_path);

    	Transform3D transform3D = new Transform3D();
    	transform3D.setScale(new Vector3d(0.4, 0.4, 0.4));
//
        Transform3D rotationY = new Transform3D();
        rotationY.rotY(Math.PI);
       // transform3D.mul(rotationY);

        Transform3D translation = new Transform3D();
        translation.setTranslation(new Vector3d(0, -1, 0));
        transform3D.mul(translation, rotationY);
//
//        transform3D.mul(rotationY, translation);
//
        TransformGroup group = new TransformGroup(transform3D);
        group.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        group.addChild(shape);
        group.addChild(shape1);
        group.addChild(shape2);
        group.addChild(shape3);
        group.addChild(shape4);
        group.setTransform(transform3D);

        TransformGroup group2 = new TransformGroup(translation);

        group2.addChild(group);

        group2.setTransform(translation);
        group2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        return group2;
       // return group;
    }
    
    private TransformGroup getModelGroup(Shape3D shape) {
    	TransformGroup group = new TransformGroup();
    	group.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    	group.addChild(shape);
    	return group;
    }
    
	private Shape3D getModelShape3D(String name, String path) throws IOException {
    	Scene scene = getSceneFromFile(path);
    	Map<String, Shape3D> map = scene.getNamedObjects();
    	printModelElementsList(map);
    	Shape3D shape = map.get(name);
    	scene.getSceneGroup().removeChild(shape);
    	return shape;
    }
    
    private Scene getSceneFromFile(String path) throws IOException {
        ObjectFile file = new ObjectFile(ObjectFile.RESIZE);
        file.setFlags(ObjectFile.RESIZE | ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY);
        return file.load(new FileReader(path));
    }
    
    private void printModelElementsList(Map<String, Shape3D> map) {
    	for (String name : map.keySet()) {
    		System.out.println("Name: " + name);
    	}
    }
    
    private void addAppearance(Shape3D shape, String path) {
    	Appearance appearance = new Appearance();
    	appearance.setTexture(getTexture(path));
        TextureAttributes attrs = new TextureAttributes();
        attrs.setTextureMode(TextureAttributes.COMBINE);
        appearance.setTextureAttributes(attrs);
        shape.setAppearance(appearance);
    }
    
    private Texture getTexture(String path) {
        TextureLoader textureLoader = new TextureLoader(path, "LUMINANCE", canvas);
        Texture texture = textureLoader.getTexture();
        texture.setBoundaryModeS(Texture.WRAP);
        texture.setBoundaryModeT(Texture.WRAP);
        texture.setBoundaryColor(new Color4f(0.0f, 1.0f, 0.0f, 0.0f));
        return texture;
    }
    
    // end main
    public static void main(String[] args) {
        try {
            Car window = new Car();
            CarAnimation boatMovement = new CarAnimation(car);
            canvas.addKeyListener(boatMovement);
            window.setVisible(true);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
