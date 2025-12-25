import {type Node} from '@tiptap/core';

export const nodeWithId = (node: Node) => {
    return node.extend({
        addAttributes() {
            return {
                id: {
                    default: null,
                    parseHTML: element => element.getAttribute('data-id'),
                    renderHTML: attrs => {
                        if (!attrs.id) return {}
                        return { 'data-id': attrs.id }
                    },
                },
            }
        },
    })
}