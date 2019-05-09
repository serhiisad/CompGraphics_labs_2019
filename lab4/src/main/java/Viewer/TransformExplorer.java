package Viewer;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TransformExplorer extends Applet implements
        Java3DExplorerConstants {

    SimpleUniverse u;

    boolean isApplication;

    Canvas3D canvas;

    OffScreenCanvas3D offScreenCanvas;

    View view;

    TransformGroup coneTG;

    // transformation factors for the cone
    Vector3f coneTranslation = new Vector3f(0.0f, 0.0f, 0.0f);

    float coneScale = 1.0f;

    Vector3d coneNUScale = new Vector3d(1.0f, 1.0f, 1.0f);

    Vector3f coneRotateAxis = new Vector3f(1.0f, 0.0f, 0.0f);

    Vector3f coneRotateNAxis = new Vector3f(1.0f, 0.0f, 0.0f);

    float coneRotateAngle = 0.0f;

    AxisAngle4f coneRotateAxisAngle = new AxisAngle4f(coneRotateAxis,
            coneRotateAngle);

    Vector3f coneRefPt = new Vector3f(0.0f, 0.0f, 0.0f);

    // this tells whether to use the compound transformation
    boolean useCompoundTransform = true;

    // These are Transforms are used for the compound transformation
    Transform3D translateTrans = new Transform3D();

    Transform3D scaleTrans = new Transform3D();

    Transform3D rotateTrans = new Transform3D();

    Transform3D refPtTrans = new Transform3D();

    Transform3D refPtInvTrans = new Transform3D();

    // this tells whether to use the uniform or non-uniform scale when
    // updating the compound transform
    boolean useUniformScale = true;

    // The size of the cone
    float coneRadius = 1.0f;

    float coneHeight = 2.0f;

    // The axis indicator, used to show the rotation axis
    RotAxis rotAxis;

    boolean showRotAxis = false;

    float rotAxisLength = 3.0f;

    // The coord sys used to show the coordinate system
    CoordSys coordSys;

    boolean showCoordSys = true;

    float coordSysLength = 5.0f;

    // GUI elements
    String rotAxisString = "Rotation Axis";

    String coordSysString = "Coord Sys";

    JCheckBox rotAxisCheckBox;

    JCheckBox coordSysCheckBox;

    String snapImageString = "Snap Image";

    String outFileBase = "transform";

    int outFileSeq = 0;

    float offScreenScale;

    JLabel coneRotateNAxisXLabel;

    JLabel coneRotateNAxisYLabel;

    JLabel coneRotateNAxisZLabel;

    // Temporaries that are reused
    Transform3D tmpTrans = new Transform3D();

    Vector3f tmpVector = new Vector3f();

    AxisAngle4f tmpAxisAngle = new AxisAngle4f();

    // geometric constant
    Point3f origin = new Point3f();

    Vector3f yAxis = new Vector3f(0.0f, 1.0f, 0.0f);

    // Returns the TransformGroup we will be editing to change the transform
    // on the cone
    TransformGroup createConeTransformGroup() {

        // create a TransformGroup for the cone, allow tranform changes,
        coneTG = new TransformGroup();
        coneTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        coneTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        // Set up an appearance to make the Cone with red ambient,
        // black emmissive, red diffuse and white specular coloring
        Material material = new Material(red, black, red, white, 64);
        // These are the colors used for the book figures:
        //Material material = new Material(white, black, white, black, 64);
        Appearance appearance = new Appearance();
        appearance.setMaterial(material);

        // create the cone and add it to the coneTG
        Cone cone = new Cone(coneRadius, coneHeight, appearance);
        coneTG.addChild(cone);

        return coneTG;
    }

    void setConeTranslation() {
        coneTG.getTransform(tmpTrans); // get the old transform
        tmpTrans.setTranslation(coneTranslation); // set only translation
        coneTG.setTransform(tmpTrans); // set the new transform
    }

    void setConeUScale() {
        coneTG.getTransform(tmpTrans); // get the old transform
        tmpTrans.setScale(coneScale); // set only scale
        coneTG.setTransform(tmpTrans); // set the new transform
    }

    void setConeNUScale() {
        coneTG.getTransform(tmpTrans); // get the old transform
        System.out.println("coneNUScale.x = " + coneNUScale.x);
        tmpTrans.setScale(coneNUScale);// set only scale
        coneTG.setTransform(tmpTrans); // set the new transform
    }

    void setConeRotation() {
        coneTG.getTransform(tmpTrans); // get the old transform
        tmpTrans.setRotation(coneRotateAxisAngle); // set only rotation
        coneTG.setTransform(tmpTrans); // set the new transform
    }

    void updateUsingCompoundTransform() {
        // set the component transformations
        translateTrans.set(coneTranslation);
        if (useUniformScale) {
            scaleTrans.set(coneScale);
        } else {
            scaleTrans.setIdentity();
            scaleTrans.setScale(coneNUScale);
        }
        rotateTrans.set(coneRotateAxisAngle);

        // translate from ref pt to origin
        tmpVector.sub(origin, coneRefPt); // vector from ref pt to origin
        refPtTrans.set(tmpVector);

        // translate from origin to ref pt
        tmpVector.sub(coneRefPt, origin); // vector from origin to ref pt
        refPtInvTrans.set(tmpVector);

        // now build up the transfomation
        // trans = translate * refPtInv * scale * rotate * refPt;
        tmpTrans.set(translateTrans);
        tmpTrans.mul(refPtInvTrans);
        tmpTrans.mul(scaleTrans);
        tmpTrans.mul(rotateTrans);
        tmpTrans.mul(refPtTrans);

        // Copy the transform to the TransformGroup
        coneTG.setTransform(tmpTrans);
    }

    // ensure that the cone rotation axis is a unit vector
    void normalizeConeRotateAxis() {
        // normalize, watch for length == 0, if so, then use default
        float lengthSquared = coneRotateAxis.lengthSquared();
        if (lengthSquared > 0.0001) {
            coneRotateNAxis.scale((float) (1.0 / Math.sqrt(lengthSquared)),
                    coneRotateAxis);
        } else {
            coneRotateNAxis.set(1.0f, 0.0f, 0.0f);
        }
    }

    // copy the current axis and angle to the axis angle, convert angle
    // to radians
    void updateConeAxisAngle() {
        coneRotateAxisAngle.set(coneRotateNAxis, (float) Math
                .toRadians(coneRotateAngle));
    }

    void updateConeRotateNormalizedLabels() {
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        coneRotateNAxisXLabel.setText("X: " + nf.format(coneRotateNAxis.x));
        coneRotateNAxisYLabel.setText("Y: " + nf.format(coneRotateNAxis.y));
        coneRotateNAxisZLabel.setText("Z: " + nf.format(coneRotateNAxis.z));
    }

    BranchGroup createSceneGraph() {
        // Create the root of the branch graph
        BranchGroup objRoot = new BranchGroup();

        // Create a TransformGroup to scale the scene down by 3.5x
        TransformGroup objScale = new TransformGroup();
        Transform3D scaleTrans = new Transform3D();
        scaleTrans.set(1 / 3.5f); // scale down by 3.5x
        objScale.setTransform(scaleTrans);
        objRoot.addChild(objScale);

        // Create a TransformGroup and initialize it to the
        // identity. Enable the TRANSFORM_WRITE capability so that
        // the mouse behaviors code can modify it at runtime. Add it to the
        // root of the subgraph.
        TransformGroup objTrans = new TransformGroup();
        objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        objScale.addChild(objTrans);

        // Add the primitives to the scene
        objTrans.addChild(createConeTransformGroup()); // the cone
        rotAxis = new RotAxis(rotAxisLength); // the axis
        objTrans.addChild(rotAxis);
        coordSys = new CoordSys(coordSysLength); // the coordSys
        objTrans.addChild(coordSys);

        BoundingSphere bounds = new BoundingSphere(new Point3d(), 100.0);

        // The book used a white background for the figures
        //Background bg = new Background(new Color3f(1.0f, 1.0f, 1.0f));
        //bg.setApplicationBounds(bounds);
        //objTrans.addChild(bg);

        // Set up the ambient light
        Color3f ambientColor = new Color3f(0.1f, 0.1f, 0.1f);
        AmbientLight ambientLightNode = new AmbientLight(ambientColor);
        ambientLightNode.setInfluencingBounds(bounds);
        objRoot.addChild(ambientLightNode);

        // Set up the directional lights
        Color3f light1Color = new Color3f(1.0f, 1.0f, 1.0f);
        Vector3f light1Direction = new Vector3f(0.0f, -0.2f, -1.0f);

        DirectionalLight light1 = new DirectionalLight(light1Color,
                light1Direction);
        light1.setInfluencingBounds(bounds);
        objRoot.addChild(light1);

        return objRoot;
    }

    public TransformExplorer() {
        this(false, 1.0f);
    }

    public TransformExplorer(boolean isApplication, float initOffScreenScale) {
        this.isApplication = isApplication;
        this.offScreenScale = initOffScreenScale;
    }

    public void init() {

        setLayout(new BorderLayout());
        GraphicsConfiguration config = SimpleUniverse
                .getPreferredConfiguration();

        canvas = new Canvas3D(config);
        add("Center", canvas);

        u = new SimpleUniverse(canvas);

        if (isApplication) {
            offScreenCanvas = new OffScreenCanvas3D(config, true);
            // set the size of the off-screen canvas based on a scale
            // of the on-screen size
            Screen3D sOn = canvas.getScreen3D();
            Screen3D sOff = offScreenCanvas.getScreen3D();
            Dimension dim = sOn.getSize();
            dim.width *= offScreenScale;
            dim.height *= offScreenScale;
            sOff.setSize(dim);
            sOff.setPhysicalScreenWidth(sOn.getPhysicalScreenWidth()
                    * offScreenScale);
            sOff.setPhysicalScreenHeight(sOn.getPhysicalScreenHeight()
                    * offScreenScale);

            // attach the offscreen canvas to the view
            u.getViewer().getView().addCanvas3D(offScreenCanvas);
        }

        // Create a simple scene and attach it to the virtual universe
        BranchGroup scene = createSceneGraph();

        // get the view
        view = u.getViewer().getView();

        // This will move the ViewPlatform back a bit so the
        // objects in the scene can be viewed.
        ViewingPlatform viewingPlatform = u.getViewingPlatform();
        viewingPlatform.setNominalViewingTransform();

        // add an orbit behavior to move the viewing platform
        OrbitBehavior orbit = new OrbitBehavior(canvas, OrbitBehavior.STOP_ZOOM);
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
                100.0);
        orbit.setSchedulingBounds(bounds);
        viewingPlatform.setViewPlatformBehavior(orbit);

        u.addBranchGraph(scene);

        add("East", guiPanel());
    }

    // create a panel with a tabbed pane holding each of the edit panels
    JPanel guiPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Translation", translationPanel());
        tabbedPane.addTab("Scaling", scalePanel());
        tabbedPane.addTab("Rotation", rotationPanel());
        tabbedPane.addTab("Reference Point", refPtPanel());
        panel.add("Center", tabbedPane);
        panel.add("South", configPanel());
        return panel;
    }

    Box translationPanel() {
        Box panel = new Box(BoxLayout.Y_AXIS);

        panel.add(new LeftAlignComponent(new JLabel("Translation Offset")));

        // X translation label, slider, and value label
        FloatLabelJSlider coneTranslateXSlider = new FloatLabelJSlider("X",
                0.1f, -2.0f, 2.0f, coneTranslation.x);
        coneTranslateXSlider.setMajorTickSpacing(1.0f);
        coneTranslateXSlider.setPaintTicks(true);
        coneTranslateXSlider.addFloatListener(new FloatListener() {
            public void floatChanged(FloatEvent e) {
                coneTranslation.x = e.getValue();
                if (useCompoundTransform) {
                    updateUsingCompoundTransform();
                } else {
                    setConeTranslation();
                }
            }
        });
        panel.add(coneTranslateXSlider);

        // Y translation label, slider, and value label
        FloatLabelJSlider coneTranslateYSlider = new FloatLabelJSlider("Y",
                0.1f, -2.0f, 2.0f, coneTranslation.y);
        coneTranslateYSlider.setMajorTickSpacing(1.0f);
        coneTranslateYSlider.setPaintTicks(true);
        coneTranslateYSlider.addFloatListener(new FloatListener() {
            public void floatChanged(FloatEvent e) {
                coneTranslation.y = e.getValue();
                if (useCompoundTransform) {
                    updateUsingCompoundTransform();
                } else {
                    setConeTranslation();
                }
            }
        });
        panel.add(coneTranslateYSlider);

        // Z translation label, slider, and value label
        FloatLabelJSlider coneTranslateZSlider = new FloatLabelJSlider("Z",
                0.1f, -2.0f, 2.0f, coneTranslation.z);
        coneTranslateZSlider.setMajorTickSpacing(1.0f);
        coneTranslateZSlider.setPaintTicks(true);
        coneTranslateZSlider.addFloatListener(new FloatListener() {
            public void floatChanged(FloatEvent e) {
                coneTranslation.z = e.getValue();
                if (useCompoundTransform) {
                    updateUsingCompoundTransform();
                } else {
                    setConeTranslation();
                }
            }
        });
        panel.add(coneTranslateZSlider);

        return panel;
    }

    Box scalePanel() {
        Box panel = new Box(BoxLayout.Y_AXIS);

        // Uniform Scale
        JLabel uniform = new JLabel("Uniform Scale");
        panel.add(new LeftAlignComponent(uniform));

        FloatLabelJSlider coneScaleSlider = new FloatLabelJSlider("S:", 0.1f,
                0.0f, 3.0f, coneScale);
        coneScaleSlider.setMajorTickSpacing(1.0f);
        coneScaleSlider.setPaintTicks(true);
        coneScaleSlider.addFloatListener(new FloatListener() {
            public void floatChanged(FloatEvent e) {
                coneScale = e.getValue();
                useUniformScale = true;
                if (useCompoundTransform) {
                    updateUsingCompoundTransform();
                } else {
                    setConeUScale();
                }
            }
        });
        panel.add(coneScaleSlider);

        JLabel nonUniform = new JLabel("Non-Uniform Scale");
        panel.add(new LeftAlignComponent(nonUniform));

        // Non-Uniform Scale
        FloatLabelJSlider coneNUScaleXSlider = new FloatLabelJSlider("X: ",
                0.1f, 0.0f, 3.0f, (float) coneNUScale.x);
        coneNUScaleXSlider.setMajorTickSpacing(1.0f);
        coneNUScaleXSlider.setPaintTicks(true);
        coneNUScaleXSlider.addFloatListener(new FloatListener() {
            public void floatChanged(FloatEvent e) {
                coneNUScale.x = (double) e.getValue();
                useUniformScale = false;
                if (useCompoundTransform) {
                    updateUsingCompoundTransform();
                } else {
                    setConeNUScale();
                }
            }
        });
        panel.add(coneNUScaleXSlider);

        FloatLabelJSlider coneNUScaleYSlider = new FloatLabelJSlider("Y: ",
                0.1f, 0.0f, 3.0f, (float) coneNUScale.y);
        coneNUScaleYSlider.setMajorTickSpacing(1.0f);
        coneNUScaleYSlider.setPaintTicks(true);
        coneNUScaleYSlider.addFloatListener(new FloatListener() {
            public void floatChanged(FloatEvent e) {
                coneNUScale.y = (double) e.getValue();
                useUniformScale = false;
                if (useCompoundTransform) {
                    updateUsingCompoundTransform();
                } else {
                    setConeNUScale();
                }
            }
        });
        panel.add(coneNUScaleYSlider);

        FloatLabelJSlider coneNUScaleZSlider = new FloatLabelJSlider("Z: ",
                0.1f, 0.0f, 3.0f, (float) coneNUScale.z);
        coneNUScaleZSlider.setMajorTickSpacing(1.0f);
        coneNUScaleZSlider.setPaintTicks(true);
        coneNUScaleZSlider.addFloatListener(new FloatListener() {
            public void floatChanged(FloatEvent e) {
                coneNUScale.z = (double) e.getValue();
                useUniformScale = false;
                if (useCompoundTransform) {
                    updateUsingCompoundTransform();
                } else {
                    setConeNUScale();
                }
            }
        });
        panel.add(coneNUScaleZSlider);

        return panel;
    }

    JPanel rotationPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1));

        panel.add(new LeftAlignComponent(new JLabel("Rotation Axis")));
        FloatLabelJSlider coneRotateAxisXSlider = new FloatLabelJSlider("X: ",
                0.01f, -1.0f, 1.0f, (float) coneRotateAxis.x);
        coneRotateAxisXSlider.addFloatListener(new FloatListener() {
            public void floatChanged(FloatEvent e) {
                coneRotateAxis.x = e.getValue();
                normalizeConeRotateAxis();
                updateConeAxisAngle();
                if (useCompoundTransform) {
                    updateUsingCompoundTransform();
                } else {
                    setConeRotation();
                }
                rotAxis.setRotationAxis(coneRotateAxis);
                updateConeRotateNormalizedLabels();
            }
        });
        panel.add(coneRotateAxisXSlider);

        FloatLabelJSlider coneRotateAxisYSlider = new FloatLabelJSlider("Y: ",
                0.01f, -1.0f, 1.0f, (float) coneRotateAxis.y);
        coneRotateAxisYSlider.addFloatListener(new FloatListener() {
            public void floatChanged(FloatEvent e) {
                coneRotateAxis.y = e.getValue();
                normalizeConeRotateAxis();
                updateConeAxisAngle();
                if (useCompoundTransform) {
                    updateUsingCompoundTransform();
                } else {
                    setConeRotation();
                }
                rotAxis.setRotationAxis(coneRotateAxis);
                updateConeRotateNormalizedLabels();
            }
        });
        panel.add(coneRotateAxisYSlider);

        FloatLabelJSlider coneRotateAxisZSlider = new FloatLabelJSlider("Z: ",
                0.01f, -1.0f, 1.0f, (float) coneRotateAxis.y);
        coneRotateAxisZSlider.addFloatListener(new FloatListener() {
            public void floatChanged(FloatEvent e) {
                coneRotateAxis.z = e.getValue();
                normalizeConeRotateAxis();
                updateConeAxisAngle();
                if (useCompoundTransform) {
                    updateUsingCompoundTransform();
                } else {
                    setConeRotation();
                }
                rotAxis.setRotationAxis(coneRotateAxis);
                updateConeRotateNormalizedLabels();
            }
        });
        panel.add(coneRotateAxisZSlider);

        JLabel normalizedLabel = new JLabel("Normalized Rotation Axis");
        panel.add(new LeftAlignComponent(normalizedLabel));
        ;
        coneRotateNAxisXLabel = new JLabel("X: 1.000");
        panel.add(new LeftAlignComponent(coneRotateNAxisXLabel));
        coneRotateNAxisYLabel = new JLabel("Y: 0.000");
        panel.add(new LeftAlignComponent(coneRotateNAxisYLabel));
        coneRotateNAxisZLabel = new JLabel("Z: 0.000");
        panel.add(new LeftAlignComponent(coneRotateNAxisZLabel));
        normalizeConeRotateAxis();
        updateConeRotateNormalizedLabels();

        FloatLabelJSlider coneRotateAxisAngleSlider = new FloatLabelJSlider(
                "Angle: ", 1.0f, -180.0f, 180.0f, (float) coneRotateAngle);
        coneRotateAxisAngleSlider.addFloatListener(new FloatListener() {
            public void floatChanged(FloatEvent e) {
                coneRotateAngle = e.getValue();
                updateConeAxisAngle();
                if (useCompoundTransform) {
                    updateUsingCompoundTransform();
                } else {
                    setConeRotation();
                }
            }
        });
        panel.add(coneRotateAxisAngleSlider);

        return panel;
    }

    Box refPtPanel() {
        Box panel = new Box(BoxLayout.Y_AXIS);

        panel.add(new LeftAlignComponent(new JLabel(
                "Reference Point Coordinates")));

        // X Ref Pt
        FloatLabelJSlider coneRefPtXSlider = new FloatLabelJSlider("X", 0.1f,
                -2.0f, 2.0f, coneRefPt.x);
        coneRefPtXSlider.setMajorTickSpacing(1.0f);
        coneRefPtXSlider.setPaintTicks(true);
        coneRefPtXSlider.addFloatListener(new FloatListener() {
            public void floatChanged(FloatEvent e) {
                coneRefPt.x = e.getValue();
                useCompoundTransform = true;
                updateUsingCompoundTransform();
                rotAxis.setRefPt(coneRefPt);
            }
        });
        panel.add(coneRefPtXSlider);

        // Y Ref Pt
        FloatLabelJSlider coneRefPtYSlider = new FloatLabelJSlider("Y", 0.1f,
                -2.0f, 2.0f, coneRefPt.y);
        coneRefPtYSlider.setMajorTickSpacing(1.0f);
        coneRefPtYSlider.setPaintTicks(true);
        coneRefPtYSlider.addFloatListener(new FloatListener() {
            public void floatChanged(FloatEvent e) {
                coneRefPt.y = e.getValue();
                useCompoundTransform = true;
                updateUsingCompoundTransform();
                rotAxis.setRefPt(coneRefPt);
            }
        });
        panel.add(coneRefPtYSlider);

        // Z Ref Pt
        FloatLabelJSlider coneRefPtZSlider = new FloatLabelJSlider("Z", 0.1f,
                -2.0f, 2.0f, coneRefPt.z);
        coneRefPtZSlider.setMajorTickSpacing(1.0f);
        coneRefPtZSlider.setPaintTicks(true);
        coneRefPtZSlider.addFloatListener(new FloatListener() {
            public void floatChanged(FloatEvent e) {
                coneRefPt.z = e.getValue();
                useCompoundTransform = true;
                updateUsingCompoundTransform();
                rotAxis.setRefPt(coneRefPt);
            }
        });
        panel.add(coneRefPtZSlider);

        return panel;
    }

    JPanel configPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1));
        panel.add(new JLabel("Display annotation:"));

        // create the check boxes
        rotAxisCheckBox = new JCheckBox(rotAxisString);
        rotAxisCheckBox.setSelected(showRotAxis);
        rotAxisCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                showRotAxis = ((JCheckBox) source).isSelected();
                if (showRotAxis) {
                    rotAxis.setWhichChild(Switch.CHILD_ALL);
                } else {
                    rotAxis.setWhichChild(Switch.CHILD_NONE);
                }
            }
        });
        panel.add(rotAxisCheckBox);

        coordSysCheckBox = new JCheckBox(coordSysString);
        coordSysCheckBox.setSelected(showCoordSys);
        coordSysCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                showCoordSys = ((JCheckBox) source).isSelected();
                if (showCoordSys) {
                    coordSys.setWhichChild(Switch.CHILD_ALL);
                } else {
                    coordSys.setWhichChild(Switch.CHILD_NONE);
                }
            }
        });
        panel.add(coordSysCheckBox);

        if (isApplication) {
            JButton snapButton = new JButton(snapImageString);
            snapButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Point loc = canvas.getLocationOnScreen();
                    offScreenCanvas.setOffScreenLocation(loc);
                    Dimension dim = canvas.getSize();
                    dim.width *= offScreenScale;
                    dim.height *= offScreenScale;
                    nf.setMinimumIntegerDigits(3);
                    nf.setMaximumFractionDigits(0);
                    offScreenCanvas.snapImageFile(outFileBase
                            + nf.format(outFileSeq++), dim.width, dim.height);
                    nf.setMinimumIntegerDigits(0);
                }
            });
            panel.add(snapButton);
        }

        return panel;
    }

    public void destroy() {
        u.removeAllLocales();
    }

    // The following allows TransformExplorer to be run as an application
    // as well as an applet
    //
    public static void main(String[] args) {
        float initOffScreenScale = 2.5f;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-s")) {
                if (args.length >= (i + 1)) {
                    initOffScreenScale = Float.parseFloat(args[i + 1]);
                    i++;
                }
            }
        }
        new MainFrame(new TransformExplorer(true, initOffScreenScale), 950, 600);
    }
}
