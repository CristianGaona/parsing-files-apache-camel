# Let's generate a CSV file with 10,000 records matching the user's format.

import csv

# Define the CSV file path
file_path = 'C:/Users/Smart/Documents/large_dataset.csv'

# Generate the data
num_records = 100
data = [
    {
        'id': i,
        'name': f"User {i}",
        'age': 20 + (i % 50),  # Random age between 20 and 69
        'email': f"user{i}@example.com"
    }
    for i in range(1, num_records + 1)
]

# Write data to CSV
with open(file_path, mode='w', newline='', encoding='utf-8') as file:
    writer = csv.DictWriter(file, fieldnames=['id', 'name', 'age', 'email'])
    writer.writeheader()
    writer.writerows(data)

file_path
