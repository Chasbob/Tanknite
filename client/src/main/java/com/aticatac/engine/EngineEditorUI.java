package com.aticatac.engine;

import com.aticatac.common.components.Component;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.reflections.Reflections;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


public class EngineEditorUI extends Application {

    public ListView<String> availClasses,usedClasses,parents;
    public TextField ClassLocation, Name,ClassToAdd,Destination;

    private Set<Class<? extends Component>> classes = new HashSet<>();
    private Set<Class<? extends Component>> implementedClasses = new HashSet<>();

    @Override
    public void start(Stage primaryStage) throws Exception{

        availClasses = new ListView<>();
        usedClasses = new ListView<>();
        parents = new ListView<>();

        primaryStage.setTitle("JavaJameJengine");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/EngineEditor.fxml"))));
        primaryStage.show();

        RefreshClasses();

    }

    @FXML
    public void AvailableClassButton(MouseEvent arg0) {
        ClassToAdd.setText(availClasses.getSelectionModel().getSelectedItem());
    }

    @FXML
    public void AvailableClassButton2(MouseEvent arg0) {
        ClassToAdd.setText(usedClasses.getSelectionModel().getSelectedItem());
    }


    @FXML
    public void Load(){
        availClasses = new ListView<>();
        usedClasses = new ListView<>();
        parents = new ListView<>();

    }

    @FXML
    public void RefreshClasses(){

        availClasses.getItems().clear();

        Reflections reflections = new Reflections("com.aticatac");
        classes = reflections.getSubTypesOf(Component.class);
        for (Class c:classes) {
            availClasses.getItems().add(c.getName());
        }

    }

    public void AddClass(){
        usedClasses.getItems().clear();
        System.out.println("Adding Class");

        for (Class<? extends Component> c:classes) {
            if (ClassToAdd.getText().equals(c.getName())) {

                implementedClasses.add(c);
                System.out.println("Added: "+c.getName());
            }
        }
        for (Class<? extends Component> c:implementedClasses) {
            usedClasses.getItems().add(c.getName());

        }

    }

    public void DeleteClass(){
        usedClasses.getItems().clear();
        System.out.println("Deleting Class");

        try {
            Class<? extends Component> clazz = (Class<? extends Component>) Class.forName(ClassToAdd.toString());
            implementedClasses.remove(clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        for (Class<? extends Component> c:implementedClasses) {
            usedClasses.getItems().add(c.getName());

        }
    }

    public void Make(){
        System.out.println("Making object\n");
        String s = "";
        for (Class<? extends Component> c:implementedClasses){
            s = s + "        arrayList.add("+c.getSimpleName()+".class);\n";
        }
        String str = "" +
                "package com.aticatac.common.prefabs;\n" +
                "import com.aticatac.common.components.*;\n" +
                "import com.aticatac.common.objectsystem.GameObject;\n" +
                "\n" +
                "import java.util.ArrayList;\n" +
                "\n" +
                "public class "+Name.getText()+" extends GameObject {\n" +
                "    public "+Name.getText()+"(GameObject parent) {\n" +
                "        super(parent);\n" +
                "        ArrayList<Class<? extends Component>> arrayList = new ArrayList<>();\n" +
                s +
                "        addComponents(arrayList);\n" +
                "    }\n" +
                "}\n";

        try {
            FileWriter fileWriter = new FileWriter(Destination.getText()+"/"+Name.getText()+".java");
            fileWriter.write(str);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*try {
            GameObject obj = new GameObject(null);
            //obj.addComponents(new ArrayList<>(implementedClasses));
            Gson gson = new Gson();
            String json = gson.toJson(obj);

            //FileWriter fileWriter = new FileWriter(Destination.getText()+"/"+Name.getText()+".java");
            //fileWriter.write(json);
            //fileWriter.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }*/
    }




}
