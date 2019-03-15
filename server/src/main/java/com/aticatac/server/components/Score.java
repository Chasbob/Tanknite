package com.aticatac.server.components;

import com.aticatac.common.components.Component;
import com.aticatac.common.objectsystem.GameObject;

public class  Score extends Component {
  public int Score = 0;

  // TODO: Scoring, and work out what object score component is added to (tank objects wouldn't work unless they dont get destroyed when they die)
  public Score(GameObject componentParent) {
    super(componentParent);
  }

  public void Add(int score) {
    Score += score;
  }

  public void Set(int score) {
    Score = score;
  }

  public int Get() {
    return Score;
  }
}
