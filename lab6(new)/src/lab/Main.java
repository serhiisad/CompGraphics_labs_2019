package lab;

import javax.vecmath.*;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.behaviors.vp.*;
import com.sun.j3d.utils.image.TextureLoader;
import javax.swing.JFrame;
import com.sun.j3d.loaders.*;
import com.sun.j3d.loaders.objectfile.*;

import java.awt.*;
import java.util.Enumeration;
import java.util.Hashtable;

public class Main extends JFrame
{
    public Canvas3D myCanvas3D;

    public Main() {

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        myCanvas3D = new Canvas3D(SimpleUniverse.getPreferredConfiguration());

        SimpleUniverse simpUniv = new SimpleUniverse(myCanvas3D);

        simpUniv.getViewingPlatform().setNominalViewingTransform();

        createSceneGraph(simpUniv);

        addLight(simpUniv);

        OrbitBehavior ob = new OrbitBehavior(myCanvas3D);
        ob.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), Double.MAX_VALUE));
        simpUniv.getViewingPlatform().setViewPlatformBehavior(ob);

        setTitle("Scrat");
        setSize(700, 700);
        getContentPane().add("Center", myCanvas3D);
        setVisible(true);
    }
    public static void main(String[] args)
    {
        new Main();
    }

    public void createSceneGraph(SimpleUniverse su)
    {

        ObjectFile f = new ObjectFile(ObjectFile.RESIZE);
        Scene widowScene = null;
        try
        {
            widowScene = f.load("models/scrat.obj");
        }
        catch (Exception e)
        {
            System.out.println("File loading failed:" + e);
        }

        Transform3D scaling = new Transform3D();
        scaling.setScale(1.0/3);
        Transform3D tfScrat = new Transform3D();
        tfScrat.rotX(Math.PI/3);
        Transform3D tfScrat2 = new Transform3D();
        tfScrat2.rotY(Math.PI);
        tfScrat.mul(tfScrat2);
        tfScrat.mul(scaling);
        TransformGroup tgScrat = new TransformGroup(tfScrat);
        TransformGroup sceneGroup = new TransformGroup();


        Hashtable widowNamedObjects = widowScene.getNamedObjects();
        Enumeration enumer = widowNamedObjects.keys();
        String name;
        while (enumer.hasMoreElements())
        {
            name = (String) enumer.nextElement();
            System.out.println("Name: "+name);
        }


        Appearance handAp = new Appearance();
        setToMyDefaultAppearance(handAp, new Color3f(108 / 255f, 195 / 255f, 114 / 255f));

        Shape3D left_hand = (Shape3D) widowNamedObjects.get("left_hand");
        Shape3D right_hand = (Shape3D) widowNamedObjects.get("right_hand");

        left_hand.setAppearance(handAp);
        right_hand.setAppearance(handAp);

        Appearance tailAp = new Appearance();
        setToMyDefaultAppearance(tailAp, new Color3f(12 / 255f, 193 / 255f, 174 / 255f));

        Shape3D tale = (Shape3D) widowNamedObjects.get("tale");

        tale.setAppearance(tailAp);


        Appearance nutAp = new Appearance();
        setToMyDefaultAppearance(nutAp, new Color3f(90 / 255f, 45 / 255f, 78 / 255f));

        Shape3D nut = (Shape3D) widowNamedObjects.get("nut");

        nut.setAppearance(nutAp);

        Appearance bodyAp = new Appearance();
        setToMyDefaultAppearance(bodyAp, new Color3f(230 / 255f, 157 / 255f, 57 / 255f));

        Shape3D body = (Shape3D) widowNamedObjects.get("body");

        body.setAppearance(bodyAp);

        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.MODULATE);

        TransformGroup transformGroup = new TransformGroup();
        transformGroup.addChild(body.cloneTree());
        Transform3D bodyT = new Transform3D();
        bodyT.rotY(Math.PI/3);
        transformGroup.setTransform(bodyT);


        TransformGroup left_handGr = new TransformGroup();
        TransformGroup right_handGr = new TransformGroup();
        TransformGroup taleGr = new TransformGroup();
        TransformGroup nutGr = new TransformGroup();

        TransformGroup leftHandAdjGr = new TransformGroup();
        TransformGroup rightHandAdjGr = new TransformGroup();

        Transform3D nutT = new Transform3D();
        nutT.setTranslation(new Vector3f(-2, 0, 0));
        nutGr.setTransform(nutT);

        Transform3D leftHandT = new Transform3D();
        leftHandT.rotX(Math.PI / 4);
        leftHandAdjGr.setTransform(leftHandT);

        leftHandAdjGr.addChild(left_handGr);

        Transform3D rightHandT = new Transform3D();
        rightHandT.rotX(-Math.PI / 4);
        rightHandAdjGr.setTransform(rightHandT);

        rightHandAdjGr.addChild(right_handGr);

        Canvas3D canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        canvas.setDoubleBufferEnable(true);
        getContentPane().add(canvas, BorderLayout.CENTER);
        TextureLoader t = new TextureLoader("models/ice1.jpg", canvas);

        Background background = new Background(t.getImage());
        background.setImageScaleMode(Background.SCALE_FIT_ALL);
        BoundingSphere boundsBack = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        background.setApplicationBounds(boundsBack);
        BranchGroup back = new BranchGroup();
        back.addChild(background);
        su.addBranchGraph(back);

        left_handGr.addChild(left_hand.cloneTree());
        right_handGr.addChild(right_hand.cloneTree());
        taleGr.addChild(tale.cloneTree());
        nutGr.addChild(nut.cloneTree());


        BoundingSphere bounds = new BoundingSphere(new Point3d(120.0,250.0,100.0),Double.MAX_VALUE);
        BranchGroup theScene = new BranchGroup();
        Transform3D tCrawl = new Transform3D();
        Transform3D tCrawl1 = new Transform3D();
        tCrawl.rotY(-90D);
        tCrawl1.rotX(-90D);
        long crawlTime = 10000;
        Alpha crawlAlpha = new Alpha(1,
                Alpha.INCREASING_ENABLE,
                0,
                0, crawlTime,0,0,0,0,0);
        float crawlDistance = 3.0f;
        PositionInterpolator posICrawl = new PositionInterpolator(crawlAlpha,
                sceneGroup,tCrawl, -9.0f, crawlDistance);

        long crawlTime1 = 30000;
        Alpha crawlAlpha1 = new Alpha(1,
                Alpha.INCREASING_ENABLE,
                3000,
                0, crawlTime1,0,0,0,0,0);
        float crawlDistance1 = 15.0f;
        PositionInterpolator posICrawl1 = new PositionInterpolator(crawlAlpha1,
                sceneGroup,tCrawl1, -9.0f, crawlDistance1);


        BoundingSphere bs = new BoundingSphere(new Point3d(0.0,0.0,0.0),Double.MAX_VALUE);
        posICrawl.setSchedulingBounds(bs);
        posICrawl1.setSchedulingBounds(bs);
        sceneGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        sceneGroup.addChild(posICrawl);


        int timeStart = 500;
        int timeRotationHour = 500;

        Transform3D leftHandRotationAxis = new Transform3D();
        //leftHandRotationAxis.rotZ(Math.PI / 2);
        Alpha leftHandRotationAlpha = new Alpha(-1, Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE, timeStart, 0,
                timeRotationHour, 0, 0, timeRotationHour, 0, 0);
        RotationInterpolator leftHandRotation = new RotationInterpolator(leftHandRotationAlpha, left_handGr,
                leftHandRotationAxis, (float) Math.PI / 4, 0.0f);
        leftHandRotation.setSchedulingBounds(bounds);


        Transform3D rightHandRotationAxis = new Transform3D();
        //rightHandRotationAxis.rotZ(Math.PI / 2);
        Alpha rightHandRotationAlpha = new Alpha(-1, Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE, 0, 0,
                timeRotationHour, 0, 0, timeRotationHour, 0, 0);
        RotationInterpolator rightHandRotation = new RotationInterpolator(rightHandRotationAlpha, right_handGr,
                rightHandRotationAxis, -(float) Math.PI / 4, 0.0f);
        rightHandRotation.setSchedulingBounds(bounds);


        Transform3D taleRotationAxis = new Transform3D();
        Alpha taleRotationAlpha = new Alpha(-1, Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE, 0, 0,
                timeRotationHour, 0, 0, timeRotationHour, 0, 0);
        RotationInterpolator taleRotation = new RotationInterpolator(taleRotationAlpha, taleGr,
                taleRotationAxis, (float) Math.PI / 4, 0.0f);
        taleRotation.setSchedulingBounds(bounds);

        left_handGr.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        right_handGr.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        taleGr.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        nutGr.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        left_handGr.addChild(leftHandRotation);
        right_handGr.addChild(rightHandRotation);
        taleGr.addChild(taleRotation);

        sceneGroup.addChild(transformGroup);
        transformGroup.addChild(leftHandAdjGr);
        transformGroup.addChild(rightHandAdjGr);
        transformGroup.addChild(taleGr);
        transformGroup.addChild(nutGr);
        tgScrat.addChild(sceneGroup);
        theScene.addChild(tgScrat);

        Background bg = new Background(new Color3f(1f,1f,1f));

        bg.setApplicationBounds(bounds);
        theScene.addChild(bg);
        theScene.compile();

        su.addBranchGraph(theScene);
    }


    public static void setToMyDefaultAppearance(Appearance app, Color3f col) {
        app.setMaterial(new Material(col, col, col, col, 150.0f));
    }


    public void addLight(SimpleUniverse su)
    {
        BranchGroup bgLight = new BranchGroup();
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);
        Color3f lightColour1 = new Color3f(0.5f,1.0f,1.0f);
        Vector3f lightDir1 = new Vector3f(-1.0f,0.0f,-0.5f);
        DirectionalLight light1 = new DirectionalLight(lightColour1, lightDir1);
        light1.setInfluencingBounds(bounds);
        bgLight.addChild(light1);
        su.addBranchGraph(bgLight);
    }
}