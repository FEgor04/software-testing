import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
import numpy as np

# Set style
sns.set(style="whitegrid")
plt.rcParams.update({'font.size': 12})

# Read data
data = pd.read_csv('result.csv')

# Create output directory for plots
import os
if not os.path.exists('plots'):
    os.makedirs('plots')

# 1. Plot trigonometric functions
plt.figure(figsize=(12, 8))
trig_cols = ['sinX', 'cosX', 'secX', 'cscX', 'cotX']
available_trig_cols = [col for col in trig_cols if col in data.columns]
for col in available_trig_cols:
    plt.plot(data['x'], data[col], label=col)
plt.title('Trigonometric Functions')
plt.xlabel('x')
plt.ylabel('y')
plt.legend()
plt.grid(True)
plt.savefig('plots/trigonometric_functions.png', dpi=300, bbox_inches='tight')
plt.close()

# 2. Plot logarithmic functions
plt.figure(figsize=(12, 8))
log_cols = ['log2X', 'log3X', 'log5X', 'log10X', 'lnX']
available_log_cols = [col for col in log_cols if col in data.columns]
for col in available_log_cols:
    plt.plot(data['x'], data[col], label=col)
plt.title('Logarithmic Functions')
plt.xlabel('x')
plt.ylabel('y')
plt.legend()
plt.grid(True)
plt.savefig('plots/logarithmic_functions.png', dpi=300, bbox_inches='tight')
plt.close()

# 3. Plot x vs result
plt.figure(figsize=(12, 8))
plt.plot(data['x'], data['result'], 'r-', linewidth=2)
plt.title('X vs Result')
plt.xlabel('x')
plt.ylabel('result')
plt.grid(True)
plt.savefig('plots/x_vs_result.png', dpi=300, bbox_inches='tight')
plt.close()

# 4. Create a heatmap of correlation between all functions
plt.figure(figsize=(14, 10))
corr_data = data.drop(columns=['x'])
correlation = corr_data.corr()
mask = np.triu(np.ones_like(correlation, dtype=bool))
sns.heatmap(correlation, annot=True, cmap='coolwarm', mask=mask, vmin=-1, vmax=1, fmt='.2f')
plt.title('Correlation Between Functions')
plt.tight_layout()
plt.savefig('plots/correlation_heatmap.png', dpi=300, bbox_inches='tight')
plt.close()

# 5. Create a line chart instead of stacked area chart
plt.figure(figsize=(14, 8))
# Select a subset of columns for better visibility
cols_to_plot = ['sinX', 'cosX', 'log2X', 'lnX', 'result']
for col in cols_to_plot:
    plt.plot(data.index, data[col], label=col, linewidth=2, alpha=0.7)
plt.title('Values of Functions')
plt.xlabel('Sample Index')
plt.ylabel('Value')
plt.legend()
plt.grid(True)
plt.savefig('plots/line_chart.png', dpi=300, bbox_inches='tight')
plt.close()

# 6. Create subplots for each function
all_cols = [col for col in data.columns if col != 'x']
n_cols = 3
n_rows = (len(all_cols) + n_cols - 1) // n_cols
plt.figure(figsize=(15, n_rows * 4))

for i, col in enumerate(all_cols, 1):
    plt.subplot(n_rows, n_cols, i)
    plt.plot(data['x'], data[col])
    plt.title(col)
    plt.grid(True)
    
plt.tight_layout()
plt.savefig('plots/all_functions_subplots.png', dpi=300, bbox_inches='tight')
plt.close()

print(f"All plots have been saved to the 'plots' directory.")

# Display interactive plots if run in a Jupyter notebook
try:
    from IPython import get_ipython
    if get_ipython() is not None:
        plt.ion()
        # Show the results vs x plot as an example
        plt.figure(figsize=(12, 8))
        plt.plot(data['x'], data['result'], 'r-', linewidth=2)
        plt.title('X vs Result (Interactive)')
        plt.xlabel('x')
        plt.ylabel('result')
        plt.grid(True)
        plt.show()
except ImportError:
    pass
