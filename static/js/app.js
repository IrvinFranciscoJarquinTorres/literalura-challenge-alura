async function cargarPopulares() {
    const btn = document.querySelector('.btn-secondary');
    if (btn) {
        btn.disabled = true;
        btn.textContent = '⏳ Cargando...';
    }

    try {
        const response = await fetch('/api/cargar-populares', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' }
        });

        const data = await response.json();
        mostrarToast(`✅ Se cargaron ${data.guardados} nuevos libros!`);

        setTimeout(() => window.location.reload(), 2000);

    } catch (error) {
        mostrarToast('❌ Error al cargar libros. Verifica tu conexión.', 'error');
        if (btn) {
            btn.disabled = false;
            btn.textContent = '🚀 Cargar libros populares';
        }
    }
}

function mostrarToast(mensaje, tipo = 'success') {
    const toast = document.getElementById('toast');
    if (!toast) return;

    toast.textContent = mensaje;
    toast.className = 'toast';

    if (tipo === 'error') {
        toast.style.background = '#e74c3c';
    } else {
        toast.style.background = '#2c3e50';
    }

    setTimeout(() => {
        toast.classList.add('hidden');
    }, 3000);
}

document.addEventListener('DOMContentLoaded', () => {

    const portadas = document.querySelectorAll('.portada');
    portadas.forEach(img => {
        img.addEventListener('error', function () {
            this.style.display = 'none';
            const placeholder = document.createElement('div');
            placeholder.className = 'portada-placeholder';
            placeholder.innerHTML = '<span>📚</span>';
            this.parentNode.appendChild(placeholder);
        });
    });

    const cards = document.querySelectorAll('.libro-card, .autor-card');
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.style.opacity = '1';
                entry.target.style.transform = 'translateY(0)';
            }
        });
    }, { threshold: 0.1 });

    cards.forEach((card, index) => {
        card.style.opacity = '0';
        card.style.transform = 'translateY(20px)';
        card.style.transition = `opacity 0.4s ease ${index * 0.05}s, transform 0.4s ease ${index * 0.05}s, box-shadow 0.3s ease`;
        observer.observe(card);
    });
});