package test.task;

import javax.media.j3d.BranchGroup;

import com.sun.j3d.utils.universe.SimpleUniverse;

import javax.media.j3d.BoundingSphere;
import javax.media.j3d.DirectionalLight;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Sphere;

class Lighting {
    public static void main(String[] args) {
        new Lighting();
    }

    public Lighting() {
        SimpleUniverse universe = new SimpleUniverse();
        BranchGroup group = new BranchGroup();
        Sphere sphere = new Sphere(0.5f);
        group.addChild(sphere);

        // Створюємо зелене світло, яке світить з відстані 100м від об'єкту
        Color3f light1Color = new Color3f(0.8f, 1.1f, 0.1f); // параметри конструктору
        // - це відповідно червона, зелена та синя компоненти кольору
        BoundingSphere bounds =
                new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
                        100.0); // вказуємо сферу, внутрішній простір якої буде освітлено
        Vector3f light1Direction = new Vector3f(4.0f, -7.0f, -12.0f); //встановлюємо

        // вектор, що задає напрям освітлення
        DirectionalLight light1 = new DirectionalLight(light1Color,
                light1Direction); //створюмо власне об'єкт освітлення
        light1.setInfluencingBounds(bounds); //вказуємо, яка частина сцени має бути
        //освітлена
        group.addChild(light1); //додаємо освітлення ло сцени


        universe.getViewingPlatform().setNominalViewingTransform();
        universe.addBranchGraph(group);

    }

}

//public class Picture {
//
//
//
//
//}

//class RedCube {
//
//    public static void main(String[] args) {
//        new RedCube();
//    }
//
//    public RedCube()
//    {
//// створюємо простір, в якому будемо працювати
//        SimpleUniverse universe = new SimpleUniverse();
//// створюємо групу, в яку додаємо об'єкти для відображення
//        BranchGroup group = new BranchGroup();
//// додаємо в групу куб зі стороною, що дорівнює 0.3 від штрини вікна
//        group.addChild(new ColorCube(0.3));
//// встановлюємо точку перегляду за замовченням
//        universe.getViewingPlatform().setNominalViewingTransform();
//// додаємо створену групу у простір
//        universe.addBranchGraph(group);
//    }
//}
