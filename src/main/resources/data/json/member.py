import json
import random
from datetime import datetime
# Function to generate a unique personal ID with the format YYYYMMDD-[1-4][0-9]{6}
def generate_unique_personal_id(used_ids):
    while True:
        date_part = (datetime.now().replace(year=datetime.now().year - random.randint(1, 50))).strftime("%Y%m%d")
        random_part = f"{random.randint(1, 4)}{random.randint(100000, 999999)}"
        personal_id = f"{date_part}-{random_part}"
        if personal_id not in used_ids:
            used_ids.add(personal_id)
            return personal_id

# Function to generate a unique member ID
def generate_unique_member_id(used_ids):
    while True:
        member_id = f"{random.choice('abcdefghijklmnopqrstuvwxyz')}{random.randint(1000, 9999)}"
        if member_id not in used_ids:
            used_ids.add(member_id)
            return member_id
# Duplicate function removed. The first definition of generate_unique_personal_id is retained.

# Function to generate random member data
def generate_members(count):
    membership_types = ["Gold", "Silver", "Platinum"]
    used_personal_ids = set()
    members = []
    for i in range(1, count + 1):
        first_names = ["John", "Jane", "Alex", "Emily", "Chris", "Katie", "Michael", "Sarah", "David", "Laura"]
        last_names = ["Smith", "Johnson", "Brown", "Taylor", "Anderson", "Thomas", "Jackson", "White", "Harris", "Martin"]
        member = {
            "memberNo": i,
            "personalId": generate_unique_personal_id(used_personal_ids),
            "name": f"{random.choice(first_names)} {random.choice(last_names)}",
            "email": f"member{i}@example.com",
            "age": random.randint(18, 60),
            "membershipType": random.choice(membership_types),
            "telNo": f"+1-{random.randint(100, 999)}-{random.randint(1000, 9999)}",
            "phoneNo": f"+1-{random.randint(100, 999)}-{random.randint(1000, 9999)}",
            "address": f"{random.randint(100, 9999)} {random.choice(['Main St', 'Broadway', 'Elm St', 'Maple Ave', 'Oak St'])}, {random.choice(['New York', 'Los Angeles', 'Chicago', 'Houston', 'Phoenix'])}, USA",
            "sex": random.choice(["Male", "Female"])
        }
        members.append(member)
    return members

# Generate 100 members
members_data = {
    "members": generate_members(100)
}

# Write to JSON file
output_file = "c:/dev/workspacePack/workspaceVsCode/ashcrow/src/main/resources/data/json/member.json"
with open(output_file, "w") as file:
    json.dump(members_data, file, indent=4)

print(f"Generated 100 members and saved to {output_file}")