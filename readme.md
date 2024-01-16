# pretty basic java malware 

Maltic enables communication between a master and a slave through sockets. The master sends commands to the slave, which executes them and returns the results.

## Features

- Execute commands on the slave machine remotely.
- Change the current working directory on the slave.
- List items in the current directory.
- Print the current working directory.
- Delete files or directories.
- Take a screenshot on the slave machine remotely.

## Components

### 1. Slave

The `Slave` class represents the component running on the machine where commands are executed. It includes functionality such as:

- Listing items in a directory.
- Changing the current directory.
- Deleting files or directories.
- Taking screenshots.

### 2. Master

The `Master` class represents the component that sends commands to the slave and receives the results. It includes functionality for:

- Sending commands to the slave.
- Handling responses from the slave.
- Taking remote screenshots.

## Usage

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/Gastordia/maltic
   cd maltic
   javac *.java
   java master || java slave
   ```
   # Usage

    Follow the prompts in the Master console to enter commands.
    
    ## Available Commands
    
    - **ls:** List items in the current directory on the slave.
    - **cd [path]:** Change the current directory on the slave.
    - **pwd:** Print the current working directory on the slave.
    - **rm [file/directory]** Delete a file or directory on the slave.
    - **scrot:** Take a screenshot on the slave.
## Contributing
  Contributions are welcome! If you'd like to enhance this project, please follow these steps:
    - Fork the project.
    - Create your feature branch (git checkout -b feature/YourFeature).
    - Commit your changes (git commit -m 'Add some feature').
    - Push to the branch (git push origin feature/YourFeature).
    - Open a pull request.    
