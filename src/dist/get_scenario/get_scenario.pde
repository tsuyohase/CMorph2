Flock flock;
JSONArray droneData;
int steps = 10000;
int numDrones = 160;
int msize = 900;

public void setup() {
  size(msize, msize);
  flock = new Flock();
  for (int i = 0; i < numDrones; i++) {
    flock.addBoid(new Boid(random(width), random(height)));
  }
  droneData = new JSONArray();
}

public void draw() {
  background(255);
  flock.run();
  
  if (frameCount <= steps) {
    JSONArray stepData = new JSONArray();
    for (Boid b : flock.boids) {
      JSONObject drone = new JSONObject();
      drone.setFloat("x", b.position.x);
      drone.setFloat("y", b.position.y);
      stepData.append(drone);
    }
    droneData.append(stepData);
  } else {
    saveDataToJson();
    noLoop();
  }
}

void saveDataToJson() {
  JSONObject output = new JSONObject();
  output.setJSONArray("droneData", droneData);
  
  saveJSONObject(output, "positions/user" + numDrones + "-map" + msize + + "-step" + steps + ".json");
  println("Data saved to drone_positions.json");
}

class Boid {
  PVector position;
  PVector velocity;
  PVector acceleration;
  float r;
  float maxforce;
  float maxspeed;
  
  Boid(float x, float y) {
    acceleration = new PVector(0, 0);
    float angle = random(TWO_PI);
    velocity = new PVector(cos(angle), sin(angle));
    position = new PVector(x, y);
    r = 2.0;
    maxspeed = 2;
    maxforce = 0.03;
  }
  
  void run(ArrayList<Boid> boids) {
    flock(boids);
    update();
    borders();
    render();
  }
  
  void applyForce(PVector force) {
    acceleration.add(force);
  }
  
  void flock(ArrayList<Boid> boids) {
    PVector sep = separate(boids);
    PVector ali = align(boids);
    PVector coh = cohesion(boids);
    sep.mult(1.5);
    ali.mult(1.0);
    coh.mult(1.0);
    applyForce(sep);
    applyForce(ali);
    applyForce(coh);
  }
  
  void update() {
    velocity.add(acceleration);
    velocity.limit(maxspeed);
    position.add(velocity);
    acceleration.mult(0);
  }
  
  void borders() {
    if (position.x < -r) position.x = width+r;
    if (position.y < -r) position.y = height+r;
    if (position.x > width+r) position.x = -r;
    if (position.y > height+r) position.y = -r;
  }
  
  void render() {
    fill(200, 100);
    stroke(0);
    ellipse(position.x, position.y, 16, 16);
  }
  
  PVector separate(ArrayList<Boid> boids) {
    float desiredseparation = 25.0f;
    PVector steer = new PVector(0, 0);
    int count = 0;
    for (Boid other : boids) {
      float d = PVector.dist(position, other.position);
      if ((d > 0) && (d < desiredseparation)) {
        PVector diff = PVector.sub(position, other.position);
        diff.normalize();
        diff.div(d);
        steer.add(diff);
        count++;
      }
    }
    if (count > 0) {
      steer.div((float) count);
    }
    if (steer.mag() > 0) {
      steer.normalize();
      steer.mult(maxspeed);
      steer.sub(velocity);
      steer.limit(maxforce);
    }
    return steer;
  }
  
  PVector align(ArrayList<Boid> boids) {
    float neighbordist = 50;
    PVector sum = new PVector(0, 0);
    int count = 0;
    for (Boid other : boids) {
      float d = PVector.dist(position, other.position);
      if ((d > 0) && (d < neighbordist)) {
        sum.add(other.velocity);
        count++;
      }
    }
    if (count > 0) {
      sum.div((float) count);
      sum.normalize();
      sum.mult(maxspeed);
      PVector steer = PVector.sub(sum, velocity);
      steer.limit(maxforce);
      return steer;
    } else {
      return new PVector(0, 0);
    }
  }
  
  PVector cohesion(ArrayList<Boid> boids) {
    float neighbordist = 50;
    PVector sum = new PVector(0, 0);
    int count = 0;
    for (Boid other : boids) {
      float d = PVector.dist(position, other.position);
      if ((d > 0) && (d < neighbordist)) {
        sum.add(other.position);
        count++;
      }
    }
    if (count > 0) {
      sum.div(count);
      return seek(sum);
    } else {
      return new PVector(0, 0);
    }
  }
  
  PVector seek(PVector target) {
    PVector desired = PVector.sub(target, position);
    desired.normalize();
    desired.mult(maxspeed);
    PVector steer = PVector.sub(desired, velocity);
    steer.limit(maxforce);
    return steer;
  }
}

class Flock {
  ArrayList<Boid> boids;
  
  Flock() {
    boids = new ArrayList<Boid>();
  }
  
  void run() {
    for (Boid b : boids) {
      b.run(boids);
    }
  }
  
  void addBoid(Boid b) {
    boids.add(b);
  }
}
