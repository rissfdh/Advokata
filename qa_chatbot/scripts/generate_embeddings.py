from sentence_transformers import SentenceTransformer
import pandas as pd
import json
from tqdm import tqdm

# **Konfigurasi**
dataset_path = "data/kuhp_qa_dataset.csv"
embeddings1_path = "data/embeddings1.json"
embeddings2_path = "data/embeddings2.json"
model_name = "paraphrase-multilingual-mpnet-base-v2"

# **Load Dataset**
df = pd.read_csv(dataset_path)

# **Load Sentence Transformer Model**
sentence_model = SentenceTransformer(model_name, device="cpu")

# **Generate Embeddings**
tqdm.pandas()

# Embeddings untuk tokens dan context (embeddings1.json)
df['tokens'] = df['jawaban'].progress_apply(lambda x: sentence_model.encode(x, convert_to_tensor=False).tolist())
embeddings1 = [
    {
        "tokens": row["tokens"],
        "context": row["jawaban"]
    }
    for _, row in df.iterrows()
]

# Simpan ke file embeddings1.json
with open(embeddings1_path, "w") as f:
    json.dump(embeddings1, f, indent=4)

# Embeddings untuk question dan context (embeddings2.json)
embeddings2 = [
    {
        "question": row["pertanyaan"],
        "context": row["jawaban"]
    }
    for _, row in df.iterrows()
]

# Simpan ke file embeddings2.json
with open(embeddings2_path, "w") as f:
    json.dump(embeddings2, f, indent=4)

print(f"Embeddings1 saved to '{embeddings1_path}'")
print(f"Embeddings2 saved to '{embeddings2_path}'")
