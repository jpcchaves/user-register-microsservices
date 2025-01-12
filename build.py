import os
import threading

threads = []
applications = ['auth-service', 'orchestrator', 'email-service']

def build_application(app):
  threads.append(app)
  print(f"Building application {app}")
  os.system(f"cd {app} && ./gradlew clean build -x test")
  print(f"Application {app} finished building!")
  threads.remove(app)

def docker_compose_up():
  print("Running containers...")
  os.popen("docker-compose up -d --build").read()
  print("Pipeline finished!")

def build_applications():
  for app in applications:
    threading.Thread(target=build_application, args=(app,)).start()

def remove_remaining_containers():
  print("Cleaning containers...")
  os.system("docker-compose down -v")
  containers = os.popen('docker ps -aq').read().split('\n')
  containers.remove('')
  if len(containers) > 0:
    print("There are still {} containers created".format(containers))
    for container in containers:
        print("Stopping container {}".format(container))
        os.system("docker container stop {}".format(container))
    os.system("docker container prune -f")

if __name__ == "__main__":
    print("Pipeline started!")
    build_applications()
    while len(threads) > 0:
        pass
    remove_remaining_containers()
    threading.Thread(target=docker_compose_up).start()