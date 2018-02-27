import os;
import sys;

scriptname = __file__.split("\\")[-1]

filenames = next( os.walk("./"))[2]

for filename in filenames:
    if str(filename).endswith(".py") and filename != scriptname:
        f = []
        with open(filename,"r") as file:
            print("="*30);
            print("open: " + filename)
            print("="*30);
            for line in file:
                if line.startswith("from Server."):
                    print("founded: " + line)
                    line = line[0:len("from ")] + line[len("from Server."):]
                    print("will change on :" + line)
                f.append(line)
        with open(filename,"w") as file:
            print("="*30);
            print("change: " + filename)
            print("="*30);
            for line in f:
                #print("rewrite: " + line)
                file.write(line)
        print("="*30);
        print("saved: " + filename)
        print("="*30);

        

print("with best wishes: " + scriptname + " aka " + sys.argv[0])