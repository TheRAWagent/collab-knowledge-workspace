
export const blockNodes = new Set([
    'paragraph',
    'heading',
    'bulletList',
    'orderedList',
    'listItem',
    'taskList',
    'taskItem',
    'codeBlock',
    'blockquote',
    'horizontalRule'
])

export const isBlockNode = (type: string): boolean => {
    return blockNodes.has(type);
}
