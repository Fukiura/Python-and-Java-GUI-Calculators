"""
This Calculator program doesn't accomodate long numbers such as irrational decimals
Clear input--(only AC), and parenthesis
"""

#Necessary Imports
import tkinter as tk
from tkinter import ttk
from time import sleep
import math

#The Button, an object-concept, important for initializing buttons of the program
class Button:
    #Initializing the object Button
    def __init__(self, purp, ui, ix, iy, size, scrn):
        self.ui = ui
        self.purpose = purp
        self.button = tk.Button(ui, text=self.purpose,width=size[0],height=size[1],command = self.func)
        self.button.place(x=ix,y=iy)
        self.screen = scrn
    
    #The Button's function, uses the Screen object
    def func(self,):
        self.screen.analyze_input(self.purpose)
        self.screen.update()

#The Screen, an object-concept, important for how the calculator functions
class Screen:
    #Initializing the object Screen
    def __init__(self, x, y, size, grd):
        self.x = x
        self.y = y
        self.size = size
        self.grid = grd
        self.inputs = []
        self.char_limit = 13
        self.to_plausible_eval = []
        self.is_in_special_operation = False

    #Initialize the screen
    def draw(self,):
        self.canvas = tk.Canvas(self.grid, bg="black", height=self.size[1], width=self.size[0])
        self.canvas.place(x=self.x,y=self.y)

    #Initialize the text in the screen
    def initialize_text(self,):
        self.txt = tk.Label(self.grid,text="0", foreground="green", background="black",font=("Arial",25))
        self.txt.place(x=5,y=40)
    
    #Resets the variables required for manipulating
    def reset_inputs(self,):
        self.inputs = []
        self.to_plausible_eval = []
        self.is_in_special_operation = False

    #Appends to the last element of a list, adds by the local parameter, to
    def append_to_last_elem(self,_list,to):
        if len(_list)>-1:
            _list[-1]=_list[-1]+to
    
    #Analyze input with x as the input
    def analyze_input(self,x):
        _inputs = ("=","AC","+/-","√","#²","÷","x")
        y=x
        required_to_append = True
        
        if x=="=":
            if self.is_in_special_operation:
                self.append_to_last_elem(self.to_plausible_eval,")")
            if len(self.inputs)>0:
                try:
                    result = str(eval("".join(self.to_plausible_eval)))
                    if len(result)>self.char_limit:
                       result = result[0:self.char_limit]
                    self.inputs = [result]
                    self.to_plausible_eval = [result]
                except TypeError:
                    self.warnings(1)
                except SyntaxError:
                    self.warnings(1)
                except ZeroDivisionError:
                    self.warnings(2)
            self.is_in_special_operation = False
            required_to_append = False
            
        elif x=="AC":
            self.reset_inputs()
            required_to_append = False
        
        if self.is_in_special_operation:
            if not self.is_a_number(x) and not x=="#²":
                self.is_in_special_operation = False
                self.append_to_last_elem(self.to_plausible_eval,")")
   
        if x=="+/-":
            required_to_append = False
            try:
                if self.is_a_number(self.inputs[-1]):
                    negation = str(eval(self.inputs[-1])*-1)
                    self.inputs[-1] = negation
                    self.to_plausible_eval[-1] = negation
                else:
                    self.warnings(3)
            except IndexError:
                self.warnings(1)
            except ValueError:
                pass
        elif x=="√":
            self.is_in_special_operation = True
            if len(self.to_plausible_eval)>0 and self.is_a_number(self.inputs[-1]):
                y="*math.sqrt("
            else:
                y="math.sqrt("
            
        elif x=="#²":
            required_to_append = False
            try:
                if self.is_a_number(self.inputs[-1]):
                    self.inputs.append("²")
                    self.to_plausible_eval[-1]=str(int(self.to_plausible_eval[-1])*int(self.to_plausible_eval[-1]))
            except IndexError:
                self.warnings(1)
            
        elif x=="÷":
            y = "/"
        
        elif x=="x":
            y = "*"
            
        elif x.isdigit():
            try:
                if self.is_a_number(self.to_plausible_eval[-1]):
                    last_elem = self.to_plausible_eval[-1]
                    is_it_next_to_a_parenthesis = last_elem.find(")")>-1
                    if is_it_next_to_a_parenthesis:
                       self.to_plausible_eval.append("*")
                    self.append_to_last_elem(self.to_plausible_eval,x)
                    self.append_to_last_elem(self.inputs,x)
                    required_to_append = False
            except IndexError:
                pass
        
        if required_to_append and not self.is_it_full():
            self.inputs.append(x)
            self.to_plausible_eval.append(y)
    
    #Algorithm that checks a number, only checks the first string if its a digit
    def is_a_number(self,x):
        if x.strip("-")[0].isdigit():
            return True
        return False
    
    #Check if self.inputs is full, used to prevent an overfilled screen
    def is_it_full(self,):
        to_string = "".join(self.inputs)
        if len(to_string)>self.char_limit:
            return True
        return False
    
    #Updates the self.screen.txt with self.inputs
    def update(self,):
        to_string = "".join(self.inputs)
        is_just_digit = to_string.isdigit()
        if to_string=="":
            self.txt["text"]=0
        elif self.is_it_full():
            self.warnings(0)
        else:
            self.txt["text"]=to_string
        
    #Throws warnings based on x that updates in the self.screen.txt, includes a tiny delay
    def warnings(self,x):
        old_inputs = self.txt["text"]
        old_font = self.txt["font"]
        old_xy = (5,40)
        if x==0:
            self.txt["text"]="Display is full!"
            self.grid.update()
            self.grid.after(500)
        if x==1:
            self.txt["font"]=(old_font[0],23)
            self.txt["text"]="Syntax Error!\n RESETTING"
            self.reset_inputs()
            self.txt.place(x=25,y=25)
            self.grid.update()
            self.grid.after(500)
            self.txt["font"]=old_font
            self.txt.place(x=old_xy[0],y=old_xy[1])
        if x==2:
            self.txt["font"]=(old_font[0],16)
            self.txt["text"]="DIVISON BY 0 Error!\n RESETTING"
            self.txt.place(x=25,y=30)
            self.reset_inputs()
            self.grid.update()
            self.grid.after(500)
            self.txt["font"]=old_font
            self.txt.place(x=old_xy[0],y=old_xy[1])
        if x==3:
            self.txt["font"]=(old_font[0],20)
            self.txt["text"]="NEED A NUMBER"
            self.grid.update()
            self.grid.after(500)
            self.txt["font"]=old_font
        
        self.txt["text"]=old_inputs
        self.grid.update()

"""
        notes: width = 5 , delta y = 45 (y2-y1),y2 = final y pos, y1 = initial y pos
               height = 2 , delta x = 50 (x2-x1), same concept above
               row = [115,115+(45*1),115+(45*2),115+(45*3)]
               col = [1,50,100,150,200]
"""
#The essentials of the program, initializing buttons,ui,etc
def essentials(ui):
    button_size = (5,2)
    Display = Screen(0,5,(246,100),ui)
    Display.draw()
    Display.initialize_text()
    
    ArithmeticButtonNames = ["PlusButton","MinusButton","DivisionButton","MultiplyButton"]
    ArithmeticSigns =  ["+","-","÷","x"]

    y = 115
    for i,Name in enumerate(ArithmeticButtonNames):
        exec(f"{Name} = Button('{ArithmeticSigns[i]}',ui,200,y,{button_size},Display)")
        y+=45
    
    SquareButton = Button("#²",ui,150,115+(45*3),button_size,Display)
    PercentButton = Button("%",ui,50,115+(45*3),button_size,Display)
    SquareRtButton = Button("√",ui,1,115+(45*2),button_size,Display)
    

    Pos_NegButton = Button("+/-",ui,1,115+(45*3),button_size,Display)
    ACButton = Button("AC",ui,1,115,button_size,Display)
    EqualButton = Button("=",ui,1,160,button_size,Display)


    Number0Button = Button("0",ui,50,115,button_size,Display)
    Number1Button = Button("1",ui,100,115,button_size,Display)
    Number2Button = Button("2",ui,150,115,button_size,Display)
    Number3Button = Button("3",ui,50,160,button_size,Display)
    Number4Button = Button("4",ui,100,160,button_size,Display)
    Number5Button = Button("5",ui,150,160,button_size,Display)
    Number6Button = Button("6",ui,50,160+45,button_size,Display)
    Number7Button = Button("7",ui,100,160+45,button_size,Display)
    Number8Button = Button("8",ui,150,160+45,button_size,Display)
    Number9Button = Button("9",ui,100,160+(45*2),button_size,Display)
    
#The main part of the program
def main():
    gui = tk.Tk()
    frame = tk.Frame(gui,height=300,width=250)
    frame.grid()
    frame.grid_propagate(0)
    essentials(frame)
    gui.mainloop()

main()
