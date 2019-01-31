# Component-Based Model

This is a short guide on how to use the compoonent-based model designed for Java in the JavaJameJengine.



## GameObject

### What are GameObjects?
GameObjects are simply just containers, they contain relevent links to other Parents (```GameObject```), Children(```GameObject```) and ```Components``` which are attached to itself.

### Create Empty GameObject

To Create a GameObject simply just instansiate GameObject as below.

```Java
GameObject Tank = new GameObject(<GameObject Parent>,<String NameOfObject>);
```

### Destory GameObject

To Destroy a GameObject simply just call.

```Java
Destroy(<GameObject object>);
```

### Find GameObject

To Find a GameObject simply just call.

```Java
tank.findObject(<String name>)
```

### What Are Prefabs?

Prefabs are Classes that extend off ```GameObject``` They should only contain code that uses the Components system, an example is if you make multiple Tanks you can create a class that automatically adds all the ```Components``` for you so it doesnt have to be done manually.

This Changes This:

```Java
GameObject Tank1 = new GameObject(<GameObject Parent>,<String NameOfObject>);
Tank.addComponent(Health.class);
Tank.addComponent(AI.class);

GameObject Tank2 = new GameObject(<GameObject Parent>,<String NameOfObject>);
Tank2.addComponent(Health.class);
Tank2.addComponent(AI.class);
```

To:

```Java
package com.aticatac.common.prefabs;
import com.aticatac.common.objectsystem.GameObject;

public class Tank extends GameObject {
    public Tank(GameObject Parent,String name) {
        super(Parent,name);
        this.addComponent(Health.class);
        this.addComponent(AI.class);
    }
}
```

```Java
GameObject Tank1 = new Tank(<GameObject Parent>,<String NameOfObject>);
GameObject Tank2 = new Tank(<GameObject Parent>,<String NameOfObject>);
```



## Component

### What are Components?
```Components``` are scripts that can be attached to any ```GameObject```. Each ```Components``` does its individual part. The power comes from attaching multiple ```Components``` to the same ```GameObject``` as well as how verisitle ```Components``` are. An example of this is a new ```GameObject``` called Tank. This ```GameObject``` would contain a health ```Components```, however a tank does just have health it can also have an Engine component and Wheel ```Components``` and much more. If you would now like to make a helicopter you would then add the Health ```Components``` to the helicopter and an engine however you wouldn't add a wheel ```Components```. 

### Create Empty Component
To create empty Component simply copy and add to body of code

```Java
package com.aticatac.common.components;

import com.aticatac.common.objectsystem.GameObject;

public class <Component> extends Component {
    

}

```

### AddComponent to GameObject

To add a ```Component``` to a GameObject.

```Java
Tank.addComponent(Health.class)
```

### GetComponent from GameObject

To get a ```Component``` froma a GameObject.

```Java
Tank.getComponent(Health.class)
```

### RemoveComponent from GameObject

To remove a ```Component``` from a GameObject.

```Java
Tank.removeComponent(Health.class)
```

