package com.aticatac.common.components;

import com.aticatac.common.objectsystem.GameObject;

public class Server extends Component {

    //server will have all of the updated and synchronised data

    public Server(GameObject parent) {
        super(parent);
    }

    //map will be array in another component

    int[][] map = {};

    //then this part will store the various data about that we need to access

    //will need to know the location of all tanks

    //will need to know the health, ammo, if it has a power up, direction
}