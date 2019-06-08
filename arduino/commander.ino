// engine pins
const int MOTOR_LEFT1 = 10; // |___________ motor left pins
const int MOTOR_LEFT2 = 11; // |

const int MOTOR_RIGHT1 = 5; // |___________ motor right pins
const int MOTOR_RIGHT2 = 6; // |

char val;
char state_speed;
char state_dir;
boolean flag;
boolean dir_flag;
int speed_val;

void setup() {
    
    state_speed = '5';
    state_dir = 'f';
    speed_val = 130;
    flag = false;
    dir_flag = false;
    
    pinMode(MOTOR_LEFT1, OUTPUT);
    pinMode(MOTOR_LEFT2, OUTPUT);
    pinMode(MOTOR_RIGHT1, OUTPUT);
    pinMode(MOTOR_RIGHT2, OUTPUT);  
    Serial.begin(9600);
}
 
void loop() {
    
  while (Serial.available()) {
    val = Serial.read(); 
   
    if(val=='0' || val=='2' || val=='5' || val == '7' || val == '9'){
      set_speed(val); 
      if (dir_flag) {
         select_direction(state_dir);
      }
       
      state_speed = val;
      flag = true;
      //Serial.println(val);     
    }
    
    else if(val=='f' || val=='b' || val=='l' || val=='r'){
      
      if(!flag){
        set_speed(state_speed);
      }
      
      state_dir = val;
      select_direction(val);
      
      dir_flag = true;
     
    }
   
    else if(val == 's') {
      analogWrite(MOTOR_LEFT1, 0);    
      analogWrite(MOTOR_LEFT2, 0); 
      analogWrite(MOTOR_RIGHT1, 0);
      analogWrite(MOTOR_RIGHT2, 0);
      dir_flag = false; 
      
      //Serial.println(val);
    
      //if(!flag){
       // set_speed('0');
      //}
    
    }
    
    else if (val == 'x'){
                  
      flag = false;  
    }
  }  
}

void select_direction(char c) {
  
  switch(c){
    
    case 'b': 
      analogWrite(MOTOR_LEFT1, speed_val);    
      analogWrite(MOTOR_LEFT2, 0); 
      analogWrite(MOTOR_RIGHT1, speed_val);
      analogWrite(MOTOR_RIGHT2, 0);
      //Serial.println(speed_val);
      break;

    case 'f':
      analogWrite(MOTOR_LEFT1, 0);    
      analogWrite(MOTOR_LEFT2, speed_val); 
      analogWrite(MOTOR_RIGHT1, 0);
      analogWrite(MOTOR_RIGHT2, speed_val);
      //Serial.println(speed_val);
      break;     
    
    case 'l':
      analogWrite(MOTOR_LEFT1, speed_val);    
      analogWrite(MOTOR_LEFT2, 0); 
      analogWrite(MOTOR_RIGHT1, 0);
      analogWrite(MOTOR_RIGHT2, speed_val);
      //Serial.println(speed_val);
      break;
    
    case 'r':
      analogWrite(MOTOR_LEFT1, 0);    
      analogWrite(MOTOR_LEFT2, speed_val); 
      analogWrite(MOTOR_RIGHT1, speed_val);
      analogWrite(MOTOR_RIGHT2, 0);
      //Serial.println(speed_val);
      break;                                
  }   
}

void set_speed(char c) {
  
  switch(c){             
      case '0':
        speed_val = 0;      
        break;
        
      case '2':
        speed_val = 90;
        break;
      
      case '5':
        speed_val = 130;
        break;
        
      case '7':
        speed_val = 195;
        break;
      
      case '9':
        speed_val = 255;
        break;
  }
}
