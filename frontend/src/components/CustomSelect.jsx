import { useEffect, useRef, useState } from 'react';
import { Check, ChevronDown } from 'lucide-react';

export default function CustomSelect({
    value,
    onChange,
    options = [],
    placeholder = 'Select an option',
    ariaLabel,
    className = '',
}) {
    const [isOpen, setIsOpen] = useState(false);
    const containerRef = useRef(null);
    const selected = options.find((option) => String(option.value) === String(value));

    useEffect(() => {
        const handlePointerDown = (event) => {
            if (!containerRef.current?.contains(event.target)) setIsOpen(false);
        };
        const handleKeyDown = (event) => {
            if (event.key === 'Escape') setIsOpen(false);
        };
        document.addEventListener('mousedown', handlePointerDown);
        document.addEventListener('keydown', handleKeyDown);
        return () => {
            document.removeEventListener('mousedown', handlePointerDown);
            document.removeEventListener('keydown', handleKeyDown);
        };
    }, []);

    return (
        <div ref={containerRef} className={`relative min-w-0 ${className}`}>
            <button
                type="button"
                onClick={() => setIsOpen((previous) => !previous)}
                className="fluid-glass-surface flex w-full items-center justify-between gap-2 rounded-xl border border-slate-700 bg-[#141C2B] px-3 py-2.5 text-left text-[11px] text-slate-200 outline-none transition hover:border-cyan-400/60 focus:border-cyan-400"
                aria-label={ariaLabel}
                aria-haspopup="listbox"
                aria-expanded={isOpen}
            >
                <span className={`min-w-0 truncate ${selected ? '' : 'text-slate-500'}`}>
                    {selected?.label || placeholder}
                </span>
                <ChevronDown className={`h-3.5 w-3.5 shrink-0 text-cyan-300 transition-transform ${isOpen ? 'rotate-180' : ''}`} />
            </button>

            {isOpen && (
                <div className="fluid-glass-surface fluid-glass-menu absolute left-0 top-full z-[140] mt-1 max-h-64 w-full overflow-y-auto rounded-xl border border-cyan-500/30 bg-[#0E131F] p-1 shadow-2xl shadow-black/40" role="listbox" aria-label={ariaLabel}>
                    {options.length === 0 ? (
                        <div className="px-3 py-2 text-[11px] text-slate-500">No options available</div>
                    ) : options.map((option) => {
                        const isSelected = String(option.value) === String(value);
                        return (
                            <button
                                key={String(option.value)}
                                type="button"
                                onClick={() => {
                                    onChange(option.value);
                                    setIsOpen(false);
                                }}
                                className={`flex w-full items-center justify-between gap-2 rounded-lg px-3 py-2 text-left text-[11px] transition ${isSelected ? 'bg-cyan-500/20 text-cyan-200' : 'text-slate-300 hover:bg-cyan-500/10 hover:text-cyan-100'}`}
                                role="option"
                                aria-selected={isSelected}
                            >
                                <span className="min-w-0 truncate">{option.label}</span>
                                {isSelected && <Check className="h-3.5 w-3.5 shrink-0 text-cyan-300" />}
                            </button>
                        );
                    })}
                </div>
            )}
        </div>
    );
}
